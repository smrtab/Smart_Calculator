package calculator

import java.lang.Exception
import java.math.BigInteger
import kotlin.math.pow

enum class Operator(val type: String, val priority: Short) {

    PARENTHESES_CLOSE(")", 4),
    PARENTHESES_OPEN("(", 4),
    POWER("^", 3),
    MULTIPLY("*", 2),
    DIVIDE("/", 2),
    MODULO("%", 2),
    PLUS("+", 1),
    MINUS("-", 1),
    NULL("", 0);

    companion object {
        fun isValidType(type: String): Boolean {
            var isValid = false
            for (enum in Operator.values()) {
                if (type == enum.type) isValid = true
            }

            return isValid
        }

        fun isParentheses(type: String): Boolean {
            return Operator.getByType(type) == Operator.PARENTHESES_OPEN ||
                    Operator.getByType(type) == Operator.PARENTHESES_CLOSE
        }

        fun isPlusOrMinus(type: String): Boolean {
            return Operator.getByType(type) == Operator.PLUS ||
                    Operator.getByType(type) == Operator.MINUS
        }

        fun getByType(type: String): Operator {
            for (enum in Operator.values()) {
                if (type == enum.type) return enum
            }
            return NULL
        }

        fun comparator(operatorOne: String, operatorTwo: String): Int {
            val priorityOne = Operator.getByType(operatorOne).priority
            val priorityTwo = Operator.getByType(operatorTwo).priority

            return if (priorityOne > priorityTwo) 1 else -1
        }

        fun normalize(element: String): String {

            if (element.length > 1) {
                for (sym in element) {
                    if (!Operator.isPlusOrMinus(sym.toString()))
                        throw Exception("Invalid expression")
                }
            }

            if (!Operator.isPlusOrMinus(element[0].toString())) {
                return element
            }

            var operator = Operator.PLUS
            for (sym in element) {
                var item = Operator.getByType(sym.toString())
                operator = if (operator != item) {
                    Operator.MINUS
                } else {
                    Operator.PLUS
                }
            }

            return operator.type
        }

        fun compute(first: String, second: String, operator: String): String {
            return if (first.length > 10 || second.length > 10) {
                _compute(first.toBigInteger(), second.toBigInteger(), operator)
            } else {
                _compute(first.toInt(), second.toInt(), operator)
            }
        }

        fun _compute(first: Int, second: Int, operator: String): String {
            return when (operator) {
                Operator.MINUS.type -> second - first
                Operator.PLUS.type -> second + first
                Operator.DIVIDE.type -> second / first
                Operator.MULTIPLY.type -> second * first
                Operator.MODULO.type -> second % first
                Operator.POWER.type -> second.toDouble().pow(first).toInt()
                else -> throw Exception("Unrecognized operator")
            }.toString()
        }

        fun _compute(first: BigInteger, second: BigInteger, operator: String): String {
            return when (operator) {
                Operator.MINUS.type -> second - first
                Operator.PLUS.type -> second + first
                Operator.DIVIDE.type -> second / first
                Operator.MULTIPLY.type -> second * first
                Operator.MODULO.type -> second % first
                Operator.POWER.type -> second.pow(first.toInt())
                else -> throw Exception("Unrecognized operator")
            }.toString()
        }
    }
}