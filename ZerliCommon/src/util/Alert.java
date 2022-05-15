package util;

public enum Alert {
    PRIMARY {
        @Override
        public String toString() {
            return "primary";
        }
    },
    SECONDARY {
        @Override
        public String toString() {
            return "secondary";
        }
    },
    SUCCESS {
        @Override
        public String toString() {
            return "success";
        }
    },
    DANGER {
        @Override
        public String toString() {
            return "danger";
        }
    },
    WARNING {
        @Override
        public String toString() {
            return "warning";
        }
    },
    INFO {
        @Override
        public String toString() {
            return "info";
        }
    },
    LIGHT {
        @Override
        public String toString() {
            return "light";
        }
    },
    DARK {
        @Override
        public String toString() {
            return "dark";
        }
    }
}
