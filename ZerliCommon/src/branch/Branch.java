package branch;

import java.io.Serializable;

/**
 * This class is used to describe a branch in the system
 */
public class Branch implements Serializable {

    /**
     * The branch name
     */
    private String branch;

    public Branch(String branch) {
        this.branch = branch;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    @Override
    public String toString() {
        return "Branch{" +
                "branch='" + branch + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        Branch branch1 = (Branch) o;

        return getBranch().equals(branch1.getBranch());
    }
}