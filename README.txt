Sagar Saija

Enjoy!

This app has three states:
1. Preview (user frames and takes picture)
2. Picture (user is shown the captured image)
3. Mask (user is shown modified image)

Layout:
	The main layout is a RelativeLayout. Within the RelativeLayout layout is a FrameLayout for display and a Button for user input.
	
Classes:
MainActivity:  Extends Activity; This class handles the interactions with the user and has the control flow code.

cameraSurfaceView: Extends SurfaceView; this is where the camera preview is shown. This also handles all interactions with the camera hardware. This is created at runtime and added to the FrameLayout.

stillImage: Extends ImageView; this is where the captured and modified images are shown to the user. This also performs the face detection and applies the masks to the image.



Questions:
1. Logcat

	To use logcat you only need to call the function Log.i(). Log.i() takes as its arguments two strings. The first string taken is a tag to indicate where the log statement came from. The second string is the actual message to be displayed. In addition to Log.i() (information) there is also Log.e() (errors), Log.w() (warnings), Log.v() (verbose) and Log.d() (debug). Each of these functions takes the exact same arguments as Log.i().
	
	An example usage would be:
	
	private static final string TAG = "My Tag";
	.
	.
	.
	Log.i(TAG, "My Message");
	
2. Logcat filtering
	Once you have messages being printed, reading them can be a problem. Logcat, by default, will print all messages from all functions used. Finding your messages from within the volumes of other messages becomes tedious and difficult. 
	
	Logcat does allow for simple filtering of messages. You can select the type of messages based on which Log function was used to print them (i.e. Log.v() or Log.i()). In Android Studio this filtering is found on the top right of the logcat frame and is labelled 'Log level'.
	
	Furthermore logcat implements custom filtering. With custom filtering you can filter by the tag used to print the message. Using a custom filter will allow you to select from more options when the filtering is applied. Using this method you can filter all messages but the ones from your program. In Android Studio this is found at the top right corner of the logcat frame in a drop down menu. In the drop down menu you should select 'Edit Filter Configuration'. In the pop-up window you will be able to create, delete and edit filter to your liking.
	
Questions TL;DR

1. Logcat
	private static final string TAG = "My Tag";
	.
	.
	.
	Log.i(TAG, "My Message");
	
2. Logcat filtering
	Top right corner of logcat frame has drop down menu. Select 'Edit Filter Configurations'. Enter message tag you want to use.
	
	
	