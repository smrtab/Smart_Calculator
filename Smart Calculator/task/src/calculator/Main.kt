package calculator

import java.lang.Exception
import java.util.*

class Calculator {

    var input: String = ""

    companion object Storage{
        val postfixRow = mutableListOf<String>()
        val variables: MutableMap<String, String> = mutableMapOf<String, String>()
    }

    fun doRetrieving(): String {
        if (!Storage.variables.containsKey(this.input))
            throw Exception("Unknown variable")

        return Storage.variables[this.input]!!
    }

    fun doCommand(): String {
        val command = Command.findByType(this.input)

        if (command == Command.EMPTY)
            throw Exception("Unknown command")

        return command.message
    }

    fun doAssignment() {

        val items = this.input.split("=")

        val key = items[0].trim()
        val value = items[1].trim()

        if (items.size > 2 || !Utils.isMatchValue(value))
            throw Exception("Invalid assignment")

        if (!Utils.isMatchKey(key))
            throw Exception("Invalid identifier")

        when {
            Storage.variables.containsKey(value) -> {
                Storage.variables[key] = Storage.variables[value]!!
            }
            Utils.isConvertibleToInt(value) -> {
                Storage.variables[key] = value
            }
            else -> {
                throw Exception("Unknown variable")
            }
        }
    }

    private fun transformToPostfix(line: MutableList<String>) {
        val storage = LinkedList<String>()

        for (element in line) {
            when {
                Utils.isConvertibleToInt(element) -> {
                    Storage.postfixRow.add(element)
                }
                else -> {

                    val operator = element
                    val top = storage.peek()

                    when {
                        storage.isEmpty() ||
                                top == Operator.PARENTHESES_OPEN.type ||
                                operator == Operator.PARENTHESES_OPEN.type -> {
                            storage.push(operator)
                        }
                        operator == Operator.PARENTHESES_CLOSE.type -> {
                            while (storage.isNotEmpty() && storage.peek() != Operator.PARENTHESES_OPEN.type) {
                                Storage.postfixRow.add(storage.pop())
                            }
                            if (storage.isEmpty())
                                throw Exception("Invalid expression")

                            storage.pop()
                        }
                        Operator.comparator(operator, top) == 1 -> {
                            storage.push(operator)
                        }
                        Operator.comparator(operator, top) == -1 -> {
                            while (storage.isNotEmpty() &&
                                    storage.peek() != Operator.PARENTHESES_OPEN.type &&
                                    Operator.comparator(operator, storage.peek()) != 1
                            ) {
                                Storage.postfixRow.add(storage.pop())
                            }
                            storage.push(operator)
                        }
                    }
                }
            }
        }

        while (storage.isNotEmpty()) {
            if (Operator.isParentheses(storage.peek())) {
                throw Exception("Invalid expression")
            }
            Storage.postfixRow.add(storage.pop())
        }
    }

    fun run(): String {

        Storage.postfixRow.clear()

        val line = Parser.decompose(this.input)

        this.transformToPostfix(line)

        val numbers = LinkedList<String>()
        var result = ""
        for (item in Storage.postfixRow) {
            if (Utils.isConvertibleToInt(item)) {
                numbers.push(item)
            } else {
                result = Operator.compute(numbers.pop(), numbers.pop(), item)
                numbers.push(result)
            }
        }

        return numbers.pop()
    }

    fun isEmptyInput() = this.input == ""
    fun isExitCommand() = this.input == Command.EXIT.type
    fun isCommand() = this.input[0] == '/'
    fun isAssignment() = this.input.matches(Regex(".*=.*"))
    fun isKeyRetrieving() = Utils.isMatchKey(this.input)
}

fun main() {
    val scanner = Scanner(System.`in`)
    val calculator = Calculator()

    while (!calculator.isExitCommand()) {

        try {
            calculator.input = scanner.nextLine()
            when {
                calculator.isEmptyInput() -> continue
                calculator.isCommand() -> {
                    println(calculator.doCommand())
                }
                calculator.isAssignment() -> {
                    calculator.doAssignment()
                }
                calculator.isKeyRetrieving() -> {
                    println(calculator.doRetrieving())
                }
                else -> {
                    println(calculator.run())
                }
            }
        } catch (e: Exception) {
            println(e.message)
        }
    }
}
