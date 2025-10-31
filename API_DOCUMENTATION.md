# Tài liệu API — Learn Spring Boot

# Tài liệu API — Learn Spring Boot

Tài liệu này mô tả các endpoint CRUD cho Author, Category và Book và các thay đổi liên quan đến soft-delete / restore / force-delete.

Base path: `/api`

Tất cả endpoint trả về wrapper chung `ApiResponseDto<T>`:
- `status`: String (ví dụ "200 OK")
- `message`: String
- `response`: Dữ liệu thực tế (T)

Lưu ý quan trọng (soft-delete)
- Mọi entity giờ kế thừa `BaseEntity` có các trường audit: `createdAt`, `updatedAt`, `deletedAt`, `createdBy`, `updatedBy`, `deletedBy`.
- Thao tác xoá mặc định là soft-delete: thay vì xóa cứng, hệ thống sẽ set `deletedAt` và `deletedBy`.
- Ứng dụng ẩn các bản ghi đã soft-deleted bằng repository base / (và một số entity sử dụng `@Where`) nên chúng không xuất hiện trong kết quả `findAll`/`findById` thông thường.
- Khi xóa một parent (Author hoặc Category) hệ thống sẽ cascade soft-delete các Book liên quan (để tránh trường hợp child hiển thị mà parent đã ẩn). Controller trả về cảnh báo nếu có book bị cascade soft-deleted.

## DTO (tóm tắt)

- AuthorRequestDto
  - name: String — required, max 256

- CategoryRequestDto
  - name: String — required, max 256

- BookRequestDto
  - name: String — required, max 256
  - quantity: Integer — optional, must be >= 0
  - price: Integer — optional, must be >= 0
  - authorId: Integer — required
  - categoryIds: Set<Integer> — optional

- AuthorResponseDto
  - id: int
  - name: String

- CategoryResponseDto
  - id: int
  - name: String

- BookResponseDto
  - id: long
  - name: String
  - quantity: Integer
  - price: Integer
  - author: AuthorResponseDto
  - categories: Set<CategoryResponseDto>

## Lỗi phổ biến

- 400 Bad Request: lỗi validate input — trả về `ApiResponseDto` với `response` là danh sách message lỗi.
- 404 Not Found: tài nguyên không tồn tại (ResourceNotFoundException) — trả về `ApiResponseDto` với message mô tả.

---

## Endpoints — Author

### GET /api/authors
- Mô tả: Lấy danh sách tất cả tác giả (không bao gồm tác giả đã soft-deleted).
- Response 200: `ApiResponseDto.response` = `List<AuthorResponseDto>`

### GET /api/authors/{id}
- Mô tả: Lấy tác giả theo id (404 nếu tác giả không tồn tại hoặc đã bị soft-deleted).
- Response 200: `ApiResponseDto.response` = `AuthorResponseDto`

### POST /api/authors
- Mô tả: Tạo tác giả mới.
- Request body JSON:

```json
{
  "name": "Jane Austen"
}
```

- Validation: `name` required, max 256
- Response 201: created `AuthorResponseDto`
- Error 400: validation lỗi

### PUT /api/authors/{id}
- Mô tả: Cập nhật tác giả.
- Request body: same as POST
- Response 200: updated `AuthorResponseDto`
- Error 404: nếu id không tồn tại

### DELETE /api/authors/{id}
- Mô tả: Soft-delete tác giả. Hệ thống sẽ cascade soft-delete các `Book` có `author` là tác giả này.
- Response 200: message xác nhận.
  - Nếu có sách bị cascade soft-deleted, message sẽ có warning: e.g. "Author deleted. Warning: 3 related books were also soft-deleted."
- Error 404: nếu id không tồn tại

### POST /api/authors/{id}/restore
- Mô tả: Restore tác giả (set `deletedAt`/`deletedBy` = null) và restore các `Book` liên quan.
- Response 200: message xác nhận

### DELETE /api/authors/{id}/force
- Mô tả: Force delete (xóa cứng) tác giả và xóa cứng các `Book` liên quan trước để tránh vi phạm FK.
- Response 200: message xác nhận

---

## Endpoints — Category

Đường dẫn base: `/api/categories` — tương tự Author, nhưng cascade soft-delete áp dụng cho `Book` có `categories` chứa category đó.

### DELETE /api/categories/{id}
- Mô tả: Soft-delete category; cascade soft-delete các sách liên quan.
- Response 200: message xác nhận. Nếu cascade xảy ra message sẽ chứa warning với số sách liên quan bị soft-deleted.

### POST /api/categories/{id}/restore
- Mô tả: Restore category và restore các sách liên quan.

### DELETE /api/categories/{id}/force
- Mô tả: Force delete category và xóa cứng sách liên quan trước.

Ví dụ POST body (create category):

```json
{
  "name": "Fiction"
}
```

---

## Endpoints — Book

### GET /api/books
- Mô tả: Lấy danh sách sách (kèm author & categories). Soft-deleted books sẽ không xuất hiện.
- Response 200: `List<BookResponseDto>`

### GET /api/books/{id}
- Mô tả: Lấy sách theo id.
- Response 200: `BookResponseDto`
- Error 404 nếu không tìm thấy (hoặc sách đã bị soft-deleted).

### POST /api/books
- Mô tả: Tạo sách mới.
- Request example:

```json
{
  "name": "The Great Gatsby",
  "quantity": 10,
  "price": 150,
  "authorId": 1,
  "categoryIds": [2, 3]
}
```

- Validation:
  - `name`: required, max 256
  - `authorId`: required
  - `quantity`, `price` nếu có phải >= 0
- Response 201: created `BookResponseDto` (kèm nested author/categories)
- Error 400: validation hoặc JSON không hợp lệ
- Error 404: nếu `authorId` hoặc bất kỳ category id nào không tồn tại (ResourceNotFoundException)

### PUT /api/books/{id}
- Mô tả: Cập nhật sách.
- Request body: same shape as POST (các field có thể optional trong update)
- Behavior:
  - cập nhật name/quantity/price nếu có
  - thay author nếu `authorId` được cung cấp
  - thay toàn bộ categories nếu `categoryIds` được cung cấp
- Response 200: updated `BookResponseDto`
- Error 404: nếu book/author/category không tồn tại

### DELETE /api/books/{id}
- Mô tả: Soft-delete sách (set `deletedAt`/`deletedBy`).
- Response 200: message xác nhận
- Error 404: nếu id không tồn tại

### POST /api/books/{id}/restore
- Mô tả: Restore sách (set `deletedAt`/`deletedBy` = null).

### DELETE /api/books/{id}/force
- Mô tả: Force delete (xóa cứng) sách.

---

## Ví dụ lỗi

Validation error (400):

```json
{
  "status": "400 BAD_REQUEST",
  "message": "Registration Failed: Please provide valid data.",
  "response": [
    "Book name is required!",
    "Author id is required!"
  ]
}
```

Not found (404):

```json
{
  "status": "404 NOT_FOUND",
  "message": "Author not found with id=5",
  "response": null
}
```

Invalid JSON (400):

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid JSON format: ...",
  "path": "/api/books"
}
```

---

## Implementation notes (technical)

- Soft-delete implementation details:
  - Entities inherit `BaseEntity` with audit fields mentioned above.
  - A custom repository base (`SoftDeleteRepository` + `SoftDeleteRepositoryImpl`) filters out soft-deleted rows for common methods like `findAll` and `findById`.
  - Additionally, several entities use `@Where(clause = "deleted_at IS NULL")` to hide soft-deleted rows by default (Hibernate-specific).
- Cascade behavior:
  - Deleting an Author will soft-delete Books with that author (service-layer cascade).
  - Deleting a Category will soft-delete Books that belong to that category (service-layer cascade).
  - Restoring a parent will restore related Books.
  - Force-delete will hard-delete related Books first, then delete the parent to avoid FK constraint issues.
- Security / auditing:
  - `createdBy` / `updatedBy` / `deletedBy` are filled from `AuditorAware` using the current authenticated user (or `SYSTEM` if unauthenticated).

---

## OpenAPI starter snippet (tuỳ chọn)

Bạn có thể mở rộng `openapi.yaml` với các endpoint mới, ví dụ:

```yaml
paths:
  /api/authors/{id}:
    delete:
      summary: Soft-delete an author (cascade soft-delete books)
      parameters:
        - $ref: '#/components/parameters/id'
      responses:
        '200':
          description: Deleted (may include warning about cascaded books)
  /api/authors/{id}/restore:
    post:
      summary: Restore an author and its books
  /api/authors/{id}/force:
    delete:
      summary: Force delete author and its books (hard delete)

  /api/categories/{id}:
    delete:
      summary: Soft-delete a category (cascade soft-delete books)
    post:
      summary: Restore category and its books
    delete:
      summary: Force delete category and its books

  /api/books/{id}:
    delete:
      summary: Soft-delete a book
    post:
      summary: Restore a book
    delete:
      summary: Force delete a book

components:
  schemas:
    # keep existing schemas and add audit fields if you want to expose them in responses
    BookResponse:
      type: object
      properties:
        id: { type: integer }
        name: { type: string }
        createdAt: { type: string, format: date-time }
        updatedAt: { type: string, format: date-time }
        deletedAt: { type: string, format: date-time, nullable: true }
        createdBy: { type: string }
```

---

## Gợi ý tiếp theo

- Nếu bạn muốn, tôi có thể:
-  - Tạo file `src/main/resources/openapi.yaml` hoàn chỉnh với tất cả endpoint mới.
-  - Thêm Swagger UI (`springdoc-openapi`) để xem docs trực tiếp.
-  - Thêm ví dụ request/response chi tiết cho mỗi endpoint.

Tạo bởi automated assistant — đã cập nhật để phản ánh soft-delete, cascade và các endpoint restore/force-delete.
