package rest;

import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;

import java.util.Set;
import javax.ws.rs.core.Application;


import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.media.multipart.MultiPartMediaTypes;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.media.multipart.MultiPartFeature;


@ApplicationPath("api")
public class ApplicationConfig extends ResourceConfig  {
    public ApplicationConfig() {
        packages("rest").register(cors.CorsFilter.class);
        packages("rest").register(errorhandling.GenericExceptionMapper.class);
        packages("rest").register(org.glassfish.jersey.server.wadl.internal.WadlResource.class);
        packages("rest").register(rest.DefaultResource.class);
        packages("rest").register(rest.UserResource.class);
        packages("rest").register(rest.UploadResource.class);
        packages("rest").register(MultiPartFeature.class);
        packages("rest").register(MultiPartMediaTypes.class);
        packages("rest").register(MultiPart.class);
        packages("rest").register(security.JWTAuthenticationFilter.class);
        packages("rest").register(security.LoginEndpoint.class);
        packages("rest").register(security.RolesAllowedFilter.class);
        packages("rest").register(security.errorhandling.AuthenticationExceptionMapper.class);
        packages("rest").register(security.errorhandling.NotAuthorizedExceptionMapper.class);
    }

}


    /*
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     *//*
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(cors.CorsFilter.class);
        resources.add(errorhandling.GenericExceptionMapper.class);
        resources.add(org.glassfish.jersey.server.wadl.internal.WadlResource.class);
        resources.add(rest.DefaultResource.class);
        resources.add(rest.UserResource.class);
        resources.add(rest.UploadResource.class);
        resources.add(MultiPartFeature.class);
        resources.add(security.JWTAuthenticationFilter.class);
        resources.add(security.LoginEndpoint.class);
        resources.add(security.RolesAllowedFilter.class);
        resources.add(security.errorhandling.AuthenticationExceptionMapper.class);
        resources.add(security.errorhandling.NotAuthorizedExceptionMapper.class);
    }


}*/

