package com.example.kattamkutti

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity(), View.OnClickListener {

    var player = true
    var turncount = 0
    var boardStatus = Array(3) { IntArray(3) { -1 } }
    lateinit var board: Array<Array<Button>>
    private lateinit var vibrator: Vibrator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Initialize the vibrator
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        board = arrayOf(
            arrayOf(findViewById(R.id.b1), findViewById(R.id.b2), findViewById(R.id.b3)),
            arrayOf(findViewById(R.id.b4), findViewById(R.id.b5), findViewById(R.id.b6)),
            arrayOf(findViewById(R.id.b7), findViewById(R.id.b8), findViewById(R.id.b9))
        )
        for (i in board) {
            for (button in i) {
                button.setOnClickListener(this)
            }
        }

        findViewById<Button>(R.id.r1).setOnClickListener {
            player = true
            turncount = 0
            initialiseBoardStatus()
        }
    }

    private fun initialiseBoardStatus() {
        boardStatus = Array(3) { IntArray(3) { -1 } }
        for (i in 0..2) {
            for (j in 0..2) {
                board[i][j].isEnabled = true
                board[i][j].text = ""
            }
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.b1 -> {
                updateValue(r = 0, c = 0, player = player)
            }
            R.id.b2 -> {
                updateValue(r = 0, c = 1, player = player)
            }
            R.id.b3 -> {
                updateValue(r = 0, c = 2, player = player)
            }
            R.id.b4 -> {
                updateValue(r = 1, c = 0, player = player)
            }
            R.id.b5 -> {
                updateValue(r = 1, c = 1, player = player)
            }
            R.id.b6 -> {
                updateValue(r = 1, c = 2, player = player)
            }
            R.id.b7 -> {
                updateValue(r = 2, c = 0, player = player)
            }
            R.id.b8 -> {
                updateValue(r = 2, c = 1, player = player)
            }
            R.id.b9 -> {
                updateValue(r = 2, c = 2, player = player)
            }
        }
        turncount++
        player = !player
        if (player) {
            updateDisplay("Player X turn")
        } else {
            updateDisplay("Player O turn")
        }
        if (turncount == 9) {
            updateDisplay("DRAW")
        }
        checkWinner()
    }

    private fun updateDisplay(s: String) {
        findViewById<TextView>(R.id.tv1).text = s
        if (s.contains("Winner") || s.contains("DRAW")) {
            vibrate()
            disable()
        }
    }

    private fun vibrate() {
        if (vibrator.hasVibrator()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(200)
            }
        }
    }

    private fun updateValue(r: Int, c: Int, player: Boolean) {
        val text = if (player) "X" else "O"
        val value = if (player) 1 else 0
        board[r][c].apply {
            isEnabled = false
            setText(text)
        }
        boardStatus[r][c] = value
    }

    private fun checkWinner() {
        // horizontal
        for (i in 0..2) {
            if (boardStatus[i][0] == boardStatus[i][1] && boardStatus[i][0] == boardStatus[i][2]) {
                if (boardStatus[i][0] == 1) {
                    updateDisplay("X is Winner")
                    break
                } else if (boardStatus[i][0] == 0) {
                    updateDisplay("O is Winner")
                    break
                }
            }
        }
        // vertical
        for (i in 0..2) {
            if (boardStatus[0][i] == boardStatus[1][i] && boardStatus[0][i] == boardStatus[2][i]) {
                if (boardStatus[0][i] == 1) {
                    updateDisplay("X is Winner")
                    break
                } else if (boardStatus[0][i] == 0) {
                    updateDisplay("O is Winner")
                    break
                }
            }
        }
        // diagonal
        if (boardStatus[0][0] == boardStatus[1][1] && boardStatus[0][0] == boardStatus[2][2]) {
            if (boardStatus[0][0] == 1) {
                updateDisplay("X is Winner")
            } else if (boardStatus[0][0] == 0) {
                updateDisplay("O is Winner")
            }
        }
        if (boardStatus[0][2] == boardStatus[1][1] && boardStatus[0][2] == boardStatus[2][0]) {
            if (boardStatus[0][2] == 1) {
                updateDisplay("X is Winner")
            } else if (boardStatus[0][2] == 0) {
                updateDisplay("0 is Winner")
            }
        }
    }

    private fun disable() {
        for (i in board) {
            for (button in i) {
                button.isEnabled = false
            }
        }
    }
}
