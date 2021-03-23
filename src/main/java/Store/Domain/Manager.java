package Store.Domain;

public class Manager extends BaseEntity<Long>{
    private final Long ManagerId;
    private final String Name;
    private final float Salary;

    public Manager(Long aLong, Long managerId, String name, float salary) {
        super(aLong);
        ManagerId = managerId;
        Name = name;
        Salary = salary;
    }

    public Long getManagerId() {
        return ManagerId;
    }

    public String getName() {
        return Name;
    }
}
