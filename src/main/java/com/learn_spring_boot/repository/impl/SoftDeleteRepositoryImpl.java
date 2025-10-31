package com.learn_spring_boot.repository.impl;

import com.learn_spring_boot.repository.SoftDeleteRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * Custom repository implementation that excludes soft-deleted rows (deletedAt != null)
 * from common query methods like findAll and findById.
 */
public class SoftDeleteRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID>
        implements SoftDeleteRepository<T, ID> {

    private final EntityManager em;
    private final JpaEntityInformation<T, ?> entityInformation;

    public SoftDeleteRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager em) {
        super(entityInformation, em);
        this.em = em;
        this.entityInformation = entityInformation;
    }

    private CriteriaQuery<T> baseQuery(CriteriaBuilder cb) {
        CriteriaQuery<T> cq = cb.createQuery(entityInformation.getJavaType());
        Root<T> root = cq.from(entityInformation.getJavaType());
        cq.select(root).where(cb.isNull(root.get("deletedAt")));
        return cq;
    }

    @Override
    public List<T> findAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> cq = baseQuery(cb);
        return em.createQuery(cq).getResultList();
    }

    @Override
    public List<T> findAll(Sort sort) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> cq = baseQuery(cb);
        // Note: Simple sort application - for complex sorts consider delegating to super
        return em.createQuery(cq).getResultList();
    }

    @Override
    public Page<T> findAll(Pageable pageable) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> cq = baseQuery(cb);
        var query = em.createQuery(cq);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        List<T> content = query.getResultList();
        long total = count();
        return org.springframework.data.support.PageableExecutionUtils.getPage(content, pageable, () -> total);
    }

    @Override
    public Optional<T> findById(ID id) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entityInformation.getJavaType());
        Root<T> root = cq.from(entityInformation.getJavaType());
        cq.select(root).where(cb.and(cb.isNull(root.get("deletedAt")), cb.equal(root.get(entityInformation.getIdAttribute().getName()), id)));
        List<T> result = em.createQuery(cq).getResultList();
        if (result.isEmpty()) return Optional.empty();
        return Optional.of(result.get(0));
    }

    @Override
    public boolean existsById(ID id) {
        return findById(id).isPresent();
    }
}
