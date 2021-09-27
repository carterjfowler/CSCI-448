package com.carterfowler.carterjfowler_a2.game

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.carterfowler.carterjfowler_a2.game.GameFragmentDirections
import com.carterfowler.carterjfowler_a2.R
import com.carterfowler.carterjfowler_a2.data.Game
import com.carterfowler.carterjfowler_a2.history.GameListViewModel
import com.carterfowler.carterjfowler_a2.history.GameListViewModelFactory
import kotlin.collections.ArrayList

private const val KEY_GAME_OVER = "game_over"
private const val KEY_TURN = "turn_num"
private const val KEY_WINNER = "winner"
private const val KEY_PIECE_1 = "piece_1"
private const val KEY_PIECE_2 = "piece_2"
private const val KEY_PIECE_3 = "piece_3"
private const val KEY_PIECE_4 = "piece_4"
private const val KEY_PIECE_5 = "piece_5"
private const val KEY_PIECE_6 = "piece_6"
private const val KEY_PIECE_7 = "piece_7"
private const val KEY_PIECE_8 = "piece_8"
private const val KEY_PIECE_9 = "piece_9"


class GameFragment : Fragment()  {
    private val logTag = "448.GameFrag"

    private lateinit var turn_text: TextView
    private lateinit var game_winner: TextView
    private lateinit var play_again_button: Button
    private lateinit var return_button: Button
    private lateinit var playable_spaces: ArrayList<ImageButton>    //used to allow computer to play
    private lateinit var all_spaces: ArrayList<ImageButton>         //used to maintain the board
    private lateinit var game_board: ArrayList<Char>                //used to maintain the board across state changes
    private lateinit var prefManager : SharedPreferences
    private var game_over = false
    private var whose_turn = 1                                      //used to determine if it is player 1 or 2's turn
    private var multiplayer = false
    private var player_one_first = true
    private var player_one_is_x = true
    private lateinit var winner: String

    private lateinit var gameViewModel: GameViewModel               //viewModel to interact with database


    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(logTag, "onAttach() called")
        prefManager = PreferenceManager.getDefaultSharedPreferences(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(logTag, "onCreate() called")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(logTag, "onCreateView() called")
        val view = inflater.inflate(R.layout.game_fragment, container, false)

        turn_text = view.findViewById(R.id.turn_display)
        game_winner = view.findViewById(R.id.winner_text_view)
        play_again_button = view.findViewById(R.id.play_again_button)
        return_button = view.findViewById(R.id.return_button)

        game_board = arrayListOf<Char>('q', 'q', 'q', 'q', 'q', 'q', 'q', 'q', 'q')
        game_board[0] = savedInstanceState?.getChar(KEY_PIECE_1, 'q') ?: 'q'
        game_board[1] = savedInstanceState?.getChar(KEY_PIECE_2, 'q') ?: 'q'
        game_board[2] = savedInstanceState?.getChar(KEY_PIECE_3, 'q') ?: 'q'
        game_board[3] = savedInstanceState?.getChar(KEY_PIECE_4, 'q') ?: 'q'
        game_board[4] = savedInstanceState?.getChar(KEY_PIECE_5, 'q') ?: 'q'
        game_board[5] = savedInstanceState?.getChar(KEY_PIECE_6, 'q') ?: 'q'
        game_board[6] = savedInstanceState?.getChar(KEY_PIECE_7, 'q') ?: 'q'
        game_board[7] = savedInstanceState?.getChar(KEY_PIECE_8, 'q') ?: 'q'
        game_board[8] = savedInstanceState?.getChar(KEY_PIECE_9, 'q') ?: 'q'


        play_again_button.setOnClickListener {
            reset_game()
        }
        return_button.setOnClickListener {
            val action =
                GameFragmentDirections.actionGameFragmentToHomePageFragment2()
            findNavController().navigate(action)
        }

        game_over = savedInstanceState?.getBoolean(KEY_GAME_OVER, false) ?: false
        winner = savedInstanceState?.getString(KEY_WINNER, "") ?: ""
        if (winner == "Player 1") game_winner.setText(R.string.game_result_player1)
        else if (winner == "Player 2") game_winner.setText(R.string.game_result_player2)
        else if (winner == "Computer") game_winner.setText(R.string.game_result_computer)
        if (game_over) {
            turn_text.visibility = View.INVISIBLE
            game_winner.visibility = View.VISIBLE
            play_again_button.visibility = View.VISIBLE
            play_again_button.isEnabled = true
            return_button.visibility = View.VISIBLE
            return_button.isEnabled = true
        } else {
            turn_text.visibility = View.VISIBLE
            game_winner.visibility = View.INVISIBLE
            play_again_button.visibility = View.INVISIBLE
            play_again_button.isEnabled = false
            return_button.visibility = View.INVISIBLE
            return_button.isEnabled = false
        }
        multiplayer = prefManager.getBoolean("multiplayer", false)
        player_one_first = prefManager.getBoolean("player_one_first", true)
        player_one_is_x = prefManager.getBoolean("player_one_x", true)
        var default_turn = 1
        if (!player_one_first) default_turn = -1
        whose_turn = savedInstanceState?.getInt(KEY_TURN, default_turn) ?: default_turn

        if(multiplayer && (!player_one_first || whose_turn == -1)) {
            turn_text.setText(R.string.game_turn_2)
        } else {
            turn_text.setText(R.string.game_turn_1)
        }

        all_spaces = arrayListOf()
        var temp: ImageButton = view.findViewById(R.id.piece1)
        if (game_board[0] == 'x') temp.setImageResource(R.drawable.ic_x_piece)
        else if (game_board[0] == 'o') temp.setImageResource(R.drawable.ic_o_piece)
        temp.setOnClickListener {
            button_pressed(0)
        }
        all_spaces.add(temp)

        temp = view.findViewById(R.id.piece2)
        if (game_board[1] == 'x') temp.setImageResource(R.drawable.ic_x_piece)
        else if (game_board[1] == 'o') temp.setImageResource(R.drawable.ic_o_piece)
        temp.setOnClickListener {
            button_pressed(1)
        }
        all_spaces.add(temp)

        temp = view.findViewById(R.id.piece3)
        if (game_board[2] == 'x') temp.setImageResource(R.drawable.ic_x_piece)
        else if (game_board[2] == 'o') temp.setImageResource(R.drawable.ic_o_piece)
        temp.setOnClickListener {
            button_pressed(2)
        }
        all_spaces.add(temp)

        temp = view.findViewById(R.id.piece4)
        if (game_board[3] == 'x') temp.setImageResource(R.drawable.ic_x_piece)
        else if (game_board[3] == 'o') temp.setImageResource(R.drawable.ic_o_piece)
        temp.setOnClickListener {
            button_pressed(3)
        }
        all_spaces.add(temp)

        temp = view.findViewById(R.id.piece5)
        if (game_board[4] == 'x') temp.setImageResource(R.drawable.ic_x_piece)
        else if (game_board[4] == 'o') temp.setImageResource(R.drawable.ic_o_piece)
        temp.setOnClickListener {
            button_pressed(4)
        }
        all_spaces.add(temp)

        temp = view.findViewById(R.id.piece6)
        if (game_board[5] == 'x') temp.setImageResource(R.drawable.ic_x_piece)
        else if (game_board[5] == 'o') temp.setImageResource(R.drawable.ic_o_piece)
        temp.setOnClickListener {
            button_pressed(5)
        }
        all_spaces.add(temp)

        temp = view.findViewById(R.id.piece7)
        if (game_board[6] == 'x') temp.setImageResource(R.drawable.ic_x_piece)
        else if (game_board[6] == 'o') temp.setImageResource(R.drawable.ic_o_piece)
        temp.setOnClickListener {
            button_pressed(6)
        }
        all_spaces.add(temp)

        temp = view.findViewById(R.id.piece8)
        if (game_board[7] == 'x') temp.setImageResource(R.drawable.ic_x_piece)
        else if (game_board[7] == 'o') temp.setImageResource(R.drawable.ic_o_piece)
        temp.setOnClickListener {
            button_pressed(7)
        }
        all_spaces.add(temp)

        temp = view.findViewById(R.id.piece9)
        if (game_board[8] == 'x') temp.setImageResource(R.drawable.ic_x_piece)
        else if (game_board[8] == 'o') temp.setImageResource(R.drawable.ic_o_piece)
        temp.setOnClickListener {
            button_pressed(8)
        }
        all_spaces.add(temp)
        playable_spaces = arrayListOf()
        if (game_board[0] == 'q') playable_spaces.add(all_spaces[0])
        if (game_board[1] == 'q') playable_spaces.add(all_spaces[1])
        if (game_board[2] == 'q') playable_spaces.add(all_spaces[2])
        if (game_board[3] == 'q') playable_spaces.add(all_spaces[3])
        if (game_board[4] == 'q') playable_spaces.add(all_spaces[4])
        if (game_board[5] == 'q') playable_spaces.add(all_spaces[5])
        if (game_board[6] == 'q') playable_spaces.add(all_spaces[6])
        if (game_board[7] == 'q') playable_spaces.add(all_spaces[7])
        if (game_board[8] == 'q') playable_spaces.add(all_spaces[8])

        val factory = GameViewModelFactory(requireContext())
        gameViewModel = ViewModelProvider(this, factory).get(GameViewModel::class.java)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(logTag, "onViewCreated() called")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(logTag, "onActivityCreated() called")
    }

    override fun onStart() {
        super.onStart()
        Log.d(logTag, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(logTag, "onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(logTag, "onPause() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(logTag, "onStop() called")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(logTag, "onDestroyView() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(logTag, "onDestroy() called")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d(logTag, "onDetach() called")
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        Log.i(logTag, "onSaveInstanceState")
        savedInstanceState.putBoolean(KEY_GAME_OVER, game_over)
        savedInstanceState.putInt(KEY_TURN, whose_turn)
        savedInstanceState.putString(KEY_WINNER, winner)
        savedInstanceState.putChar(KEY_PIECE_1, game_board[0])
        savedInstanceState.putChar(KEY_PIECE_2, game_board[1])
        savedInstanceState.putChar(KEY_PIECE_3, game_board[2])
        savedInstanceState.putChar(KEY_PIECE_4, game_board[3])
        savedInstanceState.putChar(KEY_PIECE_5, game_board[4])
        savedInstanceState.putChar(KEY_PIECE_6, game_board[5])
        savedInstanceState.putChar(KEY_PIECE_7, game_board[6])
        savedInstanceState.putChar(KEY_PIECE_8, game_board[7])
        savedInstanceState.putChar(KEY_PIECE_9, game_board[8])
    }

    private fun button_pressed(piece_num: Int) {
        //what needs to go in here???
        val space = all_spaces[piece_num]
        //ensure the button can't be pressed again
        space.isEnabled = false
        if ((whose_turn == 1 && player_one_is_x) || (whose_turn == -1 && !player_one_is_x)) {
            space.setImageResource(R.drawable.ic_x_piece)
            game_board[piece_num] = 'x'
        } else {
            space.setImageResource(R.drawable.ic_o_piece)
            game_board[piece_num] = 'o'
        }
        playable_spaces.remove(space)
        if (did_win()) {
            //record data and display winning screen
            var loser : String = ""
            if (whose_turn == 1) {
                winner = "Player 1"
                if (multiplayer) loser = "Player 2"
                else loser = "Computer"
            } else if (whose_turn == -1) {
                winner = "Player 2"
                loser = "Player 1"
            }
            var winning_piece : Char
            if ((winner == "Player 1" && player_one_is_x) || (winner == "Player 2" && !player_one_is_x)) winning_piece = 'x'
            else winning_piece = 'o'
            val temp_game = Game(winner, loser, false, winning_piece)
            gameViewModel.addGame(temp_game)
            displayWin("Player")
        } else if (playable_spaces.size > 0) {  //make sure we're not having the computer go when the playable spaces is 0 - extreme edge case (happened one time)
            if (!multiplayer) {
                computer_turn()
                if (did_win()) {
                    winner = "Computer"
                    var loser = "Player 1"
                    var winning_piece : Char
                    if (player_one_is_x) winning_piece = 'o'
                    else winning_piece = 'x'
                    val temp_game = Game(winner, loser, false, winning_piece)
                    gameViewModel.addGame(temp_game)
                    displayWin("Computer")
                }
            } else {
                //swapping turns for multiplayer
                whose_turn = whose_turn * -1
                if (whose_turn == -1) {
                    turn_text.setText(R.string.game_turn_2)
                } else {
                    turn_text.setText(R.string.game_turn_1)
                }
            }
        }
        //is it a tie, once the entire board has been filled
        if(!game_board.contains('q')) {
            //double check that the last move wasn't a winning move - edge case
            if (did_win()) {
                var loser : String = ""
                if (whose_turn == 1) {
                    winner = "Player 1"
                    if (multiplayer) loser = "Player 2"
                    else loser = "Computer"
                } else if (whose_turn == -1) {
                    winner = "Player 2"
                    loser = "Player 1"
                }
                var winning_piece : Char
                if ((winner == "Player 1" && player_one_is_x) || (winner == "Player 2" && !player_one_is_x)) winning_piece = 'x'
                else winning_piece = 'o'
                val temp_game = Game(winner, loser, false, winning_piece)
                gameViewModel.addGame(temp_game)
                displayWin("Player")
            } else {
                var loser = ""
                winner = "Player 1"
                if (multiplayer) loser = "Player 2"
                else loser = "Computer"
                val temp_game = Game(winner, loser, true, 'q')
                gameViewModel.addGame(temp_game)
                displayWin("Tie")
            }
        }
    }

    private fun computer_turn() {
        val num = playable_spaces.size
        val comp_choice = (1..num).random() - 1
        val comp_piece = playable_spaces.get(comp_choice)
        val game_board_num = all_spaces.indexOf(comp_piece)
        if(player_one_is_x) {
            comp_piece.setImageResource(R.drawable.ic_o_piece)
            game_board[game_board_num] = 'o'
        } else {
            comp_piece.setImageResource(R.drawable.ic_x_piece)
            game_board[game_board_num] = 'x'
        }
        comp_piece.isEnabled = false
        playable_spaces.remove(comp_piece)
    }

    private fun did_win() : Boolean {
        //check every possible winning combination (could be done in a easier to read way, but ran out of time)
        if (game_board[0].equals('x', true) && game_board[1].equals('x', true) && game_board[2].equals('x', true)) {return true}
        else if (game_board[0].equals('o', true) && game_board[1].equals('o', true) && game_board[2].equals('o', true)) {return true}
        else if (game_board[3].equals('x', true) && game_board[4].equals('x', true) && game_board[5].equals('x', true)) {return true}
        else if (game_board[3].equals('o', true) && game_board[4].equals('o', true) && game_board[5].equals('o', true)) {return true}
        else if (game_board[6].equals('x', true) && game_board[7].equals('x', true) && game_board[8].equals('x', true)) {return true}
        else if (game_board[6].equals('o', true) && game_board[7].equals('o', true) && game_board[8].equals('o', true)) {return true}
        else if (game_board[0].equals('x', true) && game_board[3].equals('x', true) && game_board[6].equals('x', true)) {return true}
        else if (game_board[0].equals('o', true) && game_board[3].equals('o', true) && game_board[6].equals('o', true)) {return true}
        else if (game_board[1].equals('x', true) && game_board[4].equals('x', true) && game_board[7].equals('x', true)) {return true}
        else if (game_board[1].equals('o', true) && game_board[4].equals('o', true) && game_board[7].equals('o', true)) {return true}
        else if (game_board[2].equals('x', true) && game_board[5].equals('x', true) && game_board[8].equals('x', true)) {return true}
        else if (game_board[2].equals('o', true) && game_board[5].equals('o', true) && game_board[8].equals('o', true)) {return true}
        else if (game_board[0].equals('x', true) && game_board[4].equals('x', true) && game_board[8].equals('x', true)) {return true}
        else if (game_board[0].equals('o', true) && game_board[4].equals('o', true) && game_board[8].equals('o', true)) {return true}
        else if (game_board[2].equals('x', true) && game_board[4].equals('x', true) && game_board[6].equals('x', true)) {return true}
        else if (game_board[2].equals('o', true) && game_board[4].equals('o', true) && game_board[6].equals('o', true)) {return true}

        return false
    }

    private fun reset_game() {
        //reset everything
        game_over = false

        game_board[0] = 'q'
        game_board[1] = 'q'
        game_board[2] = 'q'
        game_board[3] = 'q'
        game_board[4] = 'q'
        game_board[5] = 'q'
        game_board[6] = 'q'
        game_board[7] = 'q'
        game_board[8] = 'q'

        turn_text.visibility = View.VISIBLE
        game_winner.visibility = View.INVISIBLE
        play_again_button.visibility = View.INVISIBLE
        play_again_button.isEnabled = false
        return_button.visibility = View.INVISIBLE
        return_button.isEnabled = false

        whose_turn = 1
        if (!player_one_first) whose_turn = -1

        if(multiplayer && !player_one_first) {
            turn_text.setText(R.string.game_turn_2)
        } else {
            turn_text.setText(R.string.game_turn_1)
        }

        all_spaces[0].setImageResource(R.drawable.ic_blank_piece)
        all_spaces[1].setImageResource(R.drawable.ic_blank_piece)
        all_spaces[2].setImageResource(R.drawable.ic_blank_piece)
        all_spaces[3].setImageResource(R.drawable.ic_blank_piece)
        all_spaces[4].setImageResource(R.drawable.ic_blank_piece)
        all_spaces[5].setImageResource(R.drawable.ic_blank_piece)
        all_spaces[6].setImageResource(R.drawable.ic_blank_piece)
        all_spaces[7].setImageResource(R.drawable.ic_blank_piece)
        all_spaces[8].setImageResource(R.drawable.ic_blank_piece)

        all_spaces[0].isEnabled = true
        all_spaces[1].isEnabled = true
        all_spaces[2].isEnabled = true
        all_spaces[3].isEnabled = true
        all_spaces[4].isEnabled = true
        all_spaces[5].isEnabled = true
        all_spaces[6].isEnabled = true
        all_spaces[7].isEnabled = true
        all_spaces[8].isEnabled = true

        playable_spaces = arrayListOf()
        playable_spaces.add(all_spaces[0])
        playable_spaces.add(all_spaces[1])
        playable_spaces.add(all_spaces[2])
        playable_spaces.add(all_spaces[3])
        playable_spaces.add(all_spaces[4])
        playable_spaces.add(all_spaces[5])
        playable_spaces.add(all_spaces[6])
        playable_spaces.add(all_spaces[7])
        playable_spaces.add(all_spaces[8])
    }

    private fun displayWin(player: String) {
        //adjust screen to win state and make sure you can't click on any of the game board buttons
        for (space in all_spaces) {
            space.isEnabled = false
        }
        game_over = true
        turn_text.visibility = View.INVISIBLE
        if (player == "Tie") game_winner.setText(R.string.game_result_tie)
        else if(multiplayer) {
            if (whose_turn == 1) game_winner.setText(R.string.game_result_player1)
            else game_winner.setText(R.string.game_result_player2)
        } else if (player == "Player") game_winner.setText(R.string.game_result_player1)
        else game_winner.setText(R.string.game_result_computer)
        game_winner.visibility = View.VISIBLE
        play_again_button.visibility = View.VISIBLE
        play_again_button.isEnabled = true
        return_button.visibility = View.VISIBLE
        return_button.isEnabled = true
    }
}