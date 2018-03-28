package repository;

public class EntityNotFoundException extends Exception {

    private Long entityId;
    private String entityClass;

    public EntityNotFoundException(Long entityId, String entityClass) {
        this.entityId = entityId;
        this.entityClass = entityClass;
    }

    public Long getEntityId(){
        return entityId;
    }

    public String getEntityClass() {
        return entityClass;
    }

    public void setEntityClass(String entityClass) {
        this.entityClass = entityClass;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }
}
