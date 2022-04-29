package branch;

import java.io.Serializable;

public class Branch implements Serializable {
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
        if (o == null || getClass() != o.getClass()) return false;

        Branch branch1 = (Branch) o;

        return getBranch().equals(branch1.getBranch());
    }
}
