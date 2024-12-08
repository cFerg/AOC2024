import java.time.YearMonth

fun main(){
    monthsRemaining(6.25, 23806.64, 1300.0, YearMonth.now())
    monthsRemaining(5.0, 23806.64, 1300.0, YearMonth.now())
    monthsRemaining(4.0, 23806.64, 1300.0, YearMonth.now())
    monthsRemaining(3.0, 23806.64, 1300.0, YearMonth.now())
    monthsRemaining(2.0, 23806.64, 1300.0, YearMonth.now())
    monthsRemaining(1.0, 23806.64, 1300.0, YearMonth.now())
}

fun monthsRemaining(apr:Double, balance:Double, payments:Double, currentMonth: YearMonth){
    val dailyRate = apr/36500
    var month = currentMonth
    var months = 0.0

    var totalInterest = 0.0
    var totalPrincipal = 0.0

    var newBalance = balance
    while (newBalance > 0.0){
        //Update balance to be based off of payment, minus interest (ie. gets principal)
        val interest = newBalance * dailyRate * month.lengthOfMonth()
        val principal = payments - interest
        newBalance -= principal
        totalInterest += interest
        totalPrincipal += principal
        month = month.plusMonths(1)
        months++
    }

    println("$balance with payments of $payments at $apr% daily interest, will take $months months. (or ${String.format("%.2f", months/12.0)} years)")
    println("You would be paying a total of ${String.format("%.2f", totalInterest)} in interest, and ${String.format("%.2f", totalPrincipal)} towards principal.\n")
}