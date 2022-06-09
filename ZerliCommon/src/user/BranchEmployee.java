package user;

import branch.Branch;

/**
 * The class represents a Branch Employee in the system
 */
public class BranchEmployee extends User {
    /**
     * the branch the employee works at
     */
    private Branch branch;

    /**
     * Determines if the employee has permissions to add customer's answer to a survey
     */
    private boolean survey;

    /**
     * Determines if the employee has permission to add/update/remove discounts of a Product(s)
     */
    private boolean discount;

    /**
     * Determines if the employee has permission to update/remove Product(s) from the catalog
     */
    private boolean catalogue;

    public BranchEmployee(User user)
    {
        super(user.getUserId(),user.getUsername(),user.getPassword(),user.getUserType(),user.isLoggedIn(),
                user.getFirstName(),user.getLastName(),user.getId(),user.getEmail(),user.getPhone());
    }

    public BranchEmployee(int userID, Branch branch, boolean survey, boolean discount, boolean catalogue) {
        this.branch = branch;
        this.survey = survey;
        this.discount = discount;
        this.catalogue = catalogue;
    }



    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public boolean isSurvey() {
        return survey;
    }

    public void setSurvey(boolean survey) {
        this.survey = survey;
    }

    public boolean isDiscount() {
        return discount;
    }

    public void setDiscount(boolean discount) {
        this.discount = discount;
    }

    public boolean isCatalogue() {
        return catalogue;
    }

    public void setCatalogue(boolean catalogue) {
        this.catalogue = catalogue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        BranchEmployee that = (BranchEmployee) o;
        return survey == that.survey && discount == that.discount && catalogue == that.catalogue && branch.equals(that.branch);
    }
}