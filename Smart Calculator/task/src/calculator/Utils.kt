package calculator

object Utils {
    fun isMatchKey(key: String) = key.matches(Regex("[a-z]+", RegexOption.IGNORE_CASE))
    fun isMatchValue(value: String) = value.matches(Regex("[a-z]+|-?\\d+", RegexOption.IGNORE_CASE))
    fun isConvertibleToInt(el: String): Boolean {
        return if (el.length > 10) {
            el.toBigIntegerOrNull() != null
        } else {
            el.toIntOrNull() != null
        }
    }
}