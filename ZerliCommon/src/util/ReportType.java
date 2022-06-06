package util;

public enum ReportType {
    MONTHLY_ORDER_REPORT{
        public String toString(){
            return "MONTHLY_ORDER_REPORT";
        }
    },
    QUARTERLY_ORDER_REPORT{
        public String toString(){
            return "QUARTERLY_ORDER_REPORT";
        }
    },
    MONTHLY_COMPLAINTS_REPORT{
        public String toString() {return "MONTHLY_COMPLAINTS_REPORT";}
    },
    QUARTERLY_COMPLAINTS_REPORT{
        public String toString() {return "QUARTERLY_COMPLAINTS_REPORT";}
    },
    MONTHLY_REVENUE_REPORT{
        public String toString() {return "MONTHLY_REVENUE_REPORT";}
    },
    QUARTERLY_REVENUE_REPORT{
        public String toString() {return "QUARTERLY_REVENUE_REPORT";}
    }
}