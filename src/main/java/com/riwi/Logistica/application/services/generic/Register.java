package com.riwi.Logistica.application.services.generic;

public interface Register<Entity, EntityRequest> {
    public Entity register(EntityRequest entityRequest);
}
