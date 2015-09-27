## WaveProgressBar

An Android progressbar update progress like wave

![WaveProgressBar](http://7xkou8.com1.z0.glb.clouddn.com/ariesLRX22Gliukun09272015110902.gif)
	
## Usage
```
<com.lonekun.waveprogressbardemo.WaveProgressBar
android:layout_gravity="center_horizontal"
android:id="@+id/progressBar"
android:layout_width="200dp"
android:layout_height="100dp" />
```
and just call setmCurrentProgress to update the progress.

`mProgressBar.setmCurrentProgress(mProgress); `

## Public methods

```
getMaxProgress() //Return the max progress.

setMaxProgress(int maxProgress) //Set the max progress.

getCurrentProgress() //Get current progress.

setmCurrentProgress(int currentProgress) //Set the current progress, the progressbar will be invalidated after this method called.

setTextColor(int textColor) //Set the text color of the progress value.

setCircleColor(int circleColor) //Set the color of the back circle.

setWaveColor(int waveColor) //Set the wave progress color.
```

