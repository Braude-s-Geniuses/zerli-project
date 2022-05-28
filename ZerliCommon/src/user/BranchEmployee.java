package user;

import branch.Branch;

public class BranchEmployee extends User {
    private Branch branch;
    private boolean survey;
    private boolean discount;
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
}