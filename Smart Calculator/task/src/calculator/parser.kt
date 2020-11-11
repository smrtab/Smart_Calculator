package calculator

import java.lang.Exception

object Parser {

    fun decompose(input: String): MutableList<String>  {

        var line = mutableListOf<String>()

        var digit = ""
        var operator = ""

        for (symbol in input) {
            var item = symbol.toString()
            when {
                item.isNullOrBlank() -> continue
                Operator.isValidType(item) -> {
                    if (digit.isNotEmpty()) {
                        this.addDigit(digit, line)
                        digit = ""
                    }

                    if (Operator.isParentheses(item)) {
                        if (operator.isNotEmpty()) {
                            line.add(Operator.normalize(operator))
                        }
                        line.add(item)
                        operator = ""
                    } else operator += item
                }
                else -> {
                    digit += item
                    if (operator.isNotEmpty()) {
                        line.add(Operator.normalize(operator))
                    }
                    operator = ""
                }
            }
        }

        if (operator.isNotEmpty()) {
            throw Exception("Invalid expression")
        }

        if (digit.isNotEmpty()) {
            this.addDigit(digit, line)
        }

        return line
    }

    fun addDigit(input: String, line: MutableList<String>) {

        var digit = input

        if (Calculator.Storage.variables.containsKey(digit)) {
            digit = Calculator.Storage.variables[digit]!!
        }

        if (!Utils.isConvertibleToInt(digit)) {
            throw Exception("Invalid expression")
        }

        line.add(digit)
    }

}