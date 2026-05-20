package CLI;

public final class CliConstants {

    public static final class TextColors{
        public final static String RESET = "\033[0m";

        public final static String BLACK = "\033[0;30m";
        public final static String RED = "\033[0;31m";
        public final static String GREEN = "\033[0;32m";
        public final static String YELLOW = "\033[0;33m";
        public final static String BLUE = "\033[0;34m";
        public final static String MAGENTA = "\033[0;35m";
        public final static String CYAN = "\033[0;36m";
        public final static String WHITE = "\033[0;37m";

        public final static String BRIGHT_BLACK = "\033[0;90m";
        public final static String BRIGHT_RED = "\033[0;91m";
        public final static String BRIGHT_GREEN = "\033[0;92m";
        public final static String BRIGHT_YELLOW = "\033[0;93m";
        public final static String BRIGHT_BLUE = "\033[0;94m";
        public final static String BRIGHT_MAGENTA = "\033[0;95m";
        public final static String BRIGHT_CYAN = "\033[0;96m";
        public final static String BRIGHT_WHITE = "\033[0;97m";
    }

    public final static class TextStyles{
        public final static String BOLD = "\033[1m";
        public final static String UNDERLINE = "\033[4m";
    }

}