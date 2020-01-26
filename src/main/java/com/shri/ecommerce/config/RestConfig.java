package com.shri.ecommerce.config;

import com.shri.ecommerce.entity.Product;
import com.shri.ecommerce.entity.ProductCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Configuration
public class RestConfig implements RepositoryRestConfigurer {

    private EntityManager entityManager;

    @Autowired
    public RestConfig(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {

        HttpMethod[] unsupportedActions = {HttpMethod.PUT, HttpMethod.POST, HttpMethod.DELETE};
        config.getExposureConfiguration()
                .forDomainType(Product.class)
                .withItemExposure((metdata, httpMethods) -> httpMethods.disable(unsupportedActions))
                .withCollectionExposure((metdata, httpMethods) -> httpMethods.disable(unsupportedActions));

        config.getExposureConfiguration()
                .forDomainType(ProductCategory.class)
                .withItemExposure((metdata, httpMethods) -> httpMethods.disable(unsupportedActions))
                .withCollectionExposure((metdata, httpMethods) -> httpMethods.disable(unsupportedActions));

        exposeIds(config);
    }

    private void exposeIds(RepositoryRestConfiguration config) {

        // get a list of all entity classes from the entity manager
        Set<EntityType<?>> entityTypes = entityManager.getMetamodel().getEntities();
        // create an array of the entity type
        List<Class> entityClasses = new ArrayList<>();
        // get the entity types for the entities
        for (EntityType entityType: entityTypes) {
            entityClasses.add(entityType.getJavaType());
        }

        // expose the the entity ids for the array of entity/domain types
        Class[] domainTypes = entityClasses.toArray(new Class[0]);
        config.exposeIdsFor(domainTypes);
    }
}
