package com.example.dicesimplicity;

import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MainActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		CreateButtons();

	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main_activity_actions, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	
	private static final int NUM_ROWS = 30; //number of rows of buttons
	private static final int NUM_COLS = 4; // number of columns of buttons
	private static final CharSequence DICE_TYPE[] = {"d4", "d6", "d8", "d10", "d12", "d20"};
	
	private PopupWindow popupWindow;
	
	
	private CountDownTimer PopUpDismissTimer;
	private boolean TimerSet = false; 
	private void getPopUpDismissTimer(long millisInFuture, long countDownInterval) 
	{ 
		TimerSet = true; //Timer is running!
		PopUpDismissTimer = new CountDownTimer(millisInFuture, countDownInterval) 
		{

			@Override
			public void onTick(long millisUntilFinished) {
				
			}

			@Override
			public void onFinish() {
				TimerSet = false; // timer is NOT running!
				popupWindow.dismiss();
			}
		};
	}

	
	private void CreateButtons() {
		int dice_roll = 1;
		int dice_type = 0; // 0-5 represent the types of die: DICE_TYPE[index], index = dice_type (0-5)
		
		final LayoutInflater inflator = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
		final View popupview = inflator.inflate(R.layout.poplayout, null);
		popupWindow = new PopupWindow(popupview, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		
		TableLayout table = (TableLayout) findViewById(R.id.tableForButtons);
		
		//Create the buttons.
		for (int row = 0; row < NUM_ROWS; row++) {
			//create a new row
			TableRow tableRow = new TableRow(this);
			table.addView(tableRow);
			
			//then, in each row, create a set of buttons
			for (int col = 0; col < NUM_COLS; col++){
				//transfer number of rolls and dice type to constants for click function				
				final int FINAL_DICE_TYPE = dice_type;
				final int FINAL_DICE_ROLL = dice_roll;
				
				//create buttons
				Button button = new Button(this);
				tableRow.addView(button);
				
				//set button text
				button.setText("" + dice_roll + DICE_TYPE[dice_type] + "");
				if (dice_type == 5) { dice_type = 0; dice_roll++;}  //increment dice_roll after every set of dice
				else { dice_type++; }
				
				button.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						//popup text
						popupWindow.showAtLocation(popupview, Gravity.CENTER, 0, 0);
						SetPopUpText(FINAL_DICE_TYPE, FINAL_DICE_ROLL);
					}

				});
				
			}
		}
		
		
	}

	
	private void SetPopUpText(final int die, final int roll) {
		final TextView tv1 = (TextView) popupWindow.getContentView().findViewById(R.id.textView1);
		//Set text
		tv1.setText("" + DiceRoll(die, roll) + "");	
		tv1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//reroll
				SetPopUpText(die, roll);
			}
		});
		if (TimerSet == true) { PopUpDismissTimer.cancel(); } //if timer is already set, cancel it
		getPopUpDismissTimer(1500, 1000);
		PopUpDismissTimer.start();
	}

	

	private String DiceRoll(final int type_of_die, final int number_to_roll) {
		int roll = 0; //value of each roll
		int total = 0; //to add results
		final int DIE[] = {4, 6, 8, 10, 12, 20};  //each type of die
		final Random rand = new Random();  //for random number
		
		for (int x = 0; x < number_to_roll; x++) {
			roll = rand.nextInt(DIE[type_of_die]) + 1;
			//SendLog(roll); 			//send to log
			total = total + roll; //add to total
			
		}

		return Integer.toString(total);
	}

}


