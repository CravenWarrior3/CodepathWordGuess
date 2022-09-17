package ecooper.codepathwordguess

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    private var wordToGuess = FourLetterWordList.getRandomFourLetterWord()
    private var curGuess = 0

    /**
     * Parameters / Fields:
     *   wordToGuess : String - the target word the user is trying to guess
     *   guess : String - what the user entered as their guess
     *
     * Returns a String with colored text, where:
     *   Green represents the right letter in the right place
     *   Yellow represents the right letter in the wrong place
     *   Red represents a letter not in the target word
     */
    private fun checkGuess(guess: String) : SpannableString {
        var result = SpannableString(guess)
        for (i in 0..3) {
            if (guess[i] == wordToGuess[i]) {
                result.setSpan(ForegroundColorSpan(Color.GREEN), i, i+1, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
            }
            else if (guess[i] in wordToGuess) {
                result.setSpan(ForegroundColorSpan(Color.YELLOW), i, i+1, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
            }
            else {
                result.setSpan(ForegroundColorSpan(Color.RED), i, i+1, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
            }
        }
        return result
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val guessWord = findViewById<TextView>(R.id.guessWord)
        val guessAction = findViewById<Button>(R.id.guessAction)
        val guessInput = findViewById<EditText>(R.id.guessInput)

        guessWord.text = wordToGuess

        val guessResults = arrayOf(
            findViewById<TextView>(R.id.guess1_result),
            findViewById<TextView>(R.id.guess2_result),
            findViewById<TextView>(R.id.guess3_result)
        )


        guessAction.setOnClickListener {
            for (tv in guessResults) {
                tv.visibility = View.INVISIBLE
            }
            guessWord.visibility = View.INVISIBLE

            curGuess = 0
            wordToGuess = FourLetterWordList.getRandomFourLetterWord()
            guessWord.text = wordToGuess
        }

        guessInput.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val text = v.text.toString().uppercase()

                if (curGuess > 2) {
                    Toast.makeText(this@MainActivity, "No more guesses, restart for a new word", Toast.LENGTH_SHORT).show()
                    return@OnEditorActionListener true
                } else if (text.length != 4) {
                    Toast.makeText(this@MainActivity, "Input must be 4 letters long", Toast.LENGTH_SHORT).show()
                    return@OnEditorActionListener true
                }

                for (c in text) {
                    if (c < 'A' || c > 'Z') {
                        Toast.makeText(this@MainActivity, "Only letters are valid input", Toast.LENGTH_SHORT).show()
                        return@OnEditorActionListener true
                    }
                }

                // Check guess and display result
                guessResults[curGuess].text = checkGuess(text)
                guessResults[curGuess].visibility = View.VISIBLE

                if (text == wordToGuess) {
                    curGuess = 3
                    guessWord.visibility = View.VISIBLE
                } else {
                    curGuess += 1
                }

                if (curGuess == 3) {
                    guessWord.visibility = View.VISIBLE
                }

                return@OnEditorActionListener true
            }
            false
        })
    }



    //Toast.makeText(this, "Clicks now worth $clickValue", Toast.LENGTH_SHORT).show()
}

