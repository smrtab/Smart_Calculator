package calculator

enum class Command(val type: String, val message: String) {
    HELP("/help", "The program calculates the sum of numbers"),
    EXIT("/exit", "Bye!"),
    EMPTY("", "");

    companion object {
        fun findByType(type: String): Command {
            for (enum in Command.values()) {
                if (type == enum.type) return enum
            }

            return EMPTY
        }
    }
}