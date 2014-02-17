package it.polimi.metalnews;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		initializeViews();
		
		Intent intent = new Intent(this, HomeActivity.class);
		startActivity(intent);
				
	}

	private void initializeViews() throws NotFoundException {
		
		ImageView ivLogo = (ImageView) findViewById(R.id.main_logo);
		Animation animLogo = AnimationUtils.loadAnimation(this, R.anim.fadein);		
		ivLogo.startAnimation(animLogo);
	}
	
}
