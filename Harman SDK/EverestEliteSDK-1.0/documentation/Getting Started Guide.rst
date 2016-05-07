Getting Started Guide (Android)
=============================== 

The JBL Everest SDK is provided for Android 3rd party developers to communicate with JBL Elite Everest headphone(abbr. headphone). The intent of this SDK is to provide tools and libraries necessary to build, test and deploy the latest applications on the Android platform.


Creating a Sample Application
--------------------------------------------------------------- 

1. Add library to your project
++++++++++++++++++++++++++++++++++++++++++++ 

- The step in Android Studio:

Import everest-elite-sdk.jar library. 

1. put everest-elite-sdk.jar in your_proj/app/libs

2. app/build.gradle

.. code-block:: java

                dependencies {
                    compile files('libs/everest-elite-sdk.jar')
                }

- The step in Eclipse:

put everest-elite-sdk.jar in your_proj/libs. This jar has been minified, but APIs appear to developers. 

3. Create Bluetooth connect and start
++++++++++++++++++++++++++++++++++++++++++++ 

We use bluetooth protocal to communicate phone and JBL everest headphone. So create bluetooth connect at first. This class
will not automatically connect the phone to the everest, rather it expects the bluetooth connection to already
exist between the phone and the headphones. The Bluetooth class will allow the app to control functions on the headphones.

.. code-block:: java

                // This class takes an instance of the BluetoothListener interface, Activity, and a boolean for secure (true) 
                // or insecure (false) RF socket connection. 
                mBluetooth = new Bluetooth(this, this, true);
                mBluetooth.start();

This will start the pairing process. If anything happens, the BluetoothListener interface methods will take care of any change in state.

4. Create headphone control from Bluetooth connect.
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
Next is getting an instance of the HeadPhoneCtrl. This is the class you'll be using in order to interface with the headphones.  
One of the functions on the BluetoothListener interface is bluetoothDeviceConnected. In that method type this:


.. code-block:: java

                //...
                // declare the variable..
                private HeadPhoneCtrl headPhoneCtrl;
                //...
                public void bluetoothDeviceConnected(Bluetooth bluetooth, BluetoothDevice bluetoothDevice, BluetoothSocket bluetoothSocket) {
                    if (headphCtrl != null && headphCtrl.getSocket().equals(bluetoothSocket)) {
                        headphCtrl.resetHeadPhoneCtrl(bluetoothSocket);
                    } else {
                        try {
                            headphCtrl.close();
                            headphCtrl = null;
                        } catch (Exception e) {
                        }
                        // use this to interface with the headphones
                        headphCtrl = HeadPhoneCtrl.getInstance(this, bluetoothSocket);
                    }
                }
                // ...

To assure headphCtrl is valid, we should confirm Bluetooth is connected.

.. code-block:: java

                //...
                @Override
                public void bluetoothDeviceDisconnected(Bluetooth bluetooth, BluetoothDevice bluetoothDevice) {
                    Log.d("EVEREST", "disconnected");
                    headPhoneCtrl = null;
                }
                // ...
	

5.  Implement HeadPhoneCtrl 
++++++++++++++++++++++++++++++++++++++++++++ 
It contains 4 basic controls: ANCCtrl, EQSettingCtrl, CalibrateCtrl, CommonCtrl corresponding to 4 listeners: ANCCtrlListner,EQSettingListner, CalibrateListener
CommonListner. Every method on these classes are asychronous, so when they are called they would need to call a method on their event listener interface   

Here's an example of how to get the battery level of the device. That's in the ANCCtrl class: 

.. code-block:: java

                //...
                headphCtrl = HeadPhoneCtrl.getInstance(this, bluetoothSocket);
                // set a listener to receive data when
                headphCtrl.setAncListner(new CustomANCCtrlListener());
                headphCtrl.getBatteryLevel();
                //...
                
                public class CustomANCCtrlListener implements ANCCtrlListner{
                    //... other interface methods...
                    
                    @Override
                    public void getBatteryLevelReply(long batteryLevel) {
                        Log.d("EVERSET", "Battery Level: " + batteryLevel);
                    }
                }

6. Enums may be used.
++++++++++++++++++++++++++++++++++++++++++++ 

ANCAwarenessPreset is used to set awareness state

.. code-block:: java

                enum ANCAwarenessPreset {
                    None(0),
                    Low(1),
                    Medium(2),
                    High(3),
                    First(0),
                    Last(3),
                    NumPresets(4);
                 }

AudioEQPreset is to set Audio EQ settings.

.. code-block:: java

                enum AudioEQPreset {
                    Music(0),
                    Gaming(1),
                    Movie(2),
                    Conference(3),
                    First(0),
                    Last(3),
                    NumPresets(4);
                }

GraphicEQPreset is to set graphic EQ Settings. 

.. code-block:: java

                enum GraphicEQPreset {
                    Off(0),
                    Jazz(1),
                    Vocal(2),
                    Bass(3),
                    User(4),
                    First(1),
                    Last(4),
                    NumPresets(4);
                }
	
7. APIs and Callbacks(Listeners)
++++++++++++++++++++++++++++++++++++++++++++ 
All the updates from the device and the results of set device are reported to the phone via callbacks. So, you must use corresponding callbacks accordingly.

========================================================= =====================================================  
HeadPhoneCtrl.ANCCtrl                     			 					interface ANCCtrlListner 
========================================================= =====================================================
switchANC(boolean onOff)                				 					none   
getANCEnable()                                	 					getANCSwitchStateReply(boolean onOff)   
getANCAwarenessPreset()                 				 					getANCAwarenessPresetReply(ANCAwarenessPreset preset)   
setANCAwarenessPreset(ANCAwarenessPreset preset) 					none 
setLeftAwarenessPresetValue(int leftANCvalue)    					none
setRightAwarenessPresetValue(int rightANCvalue)  					none 
getLeftANCvalue()                                					getLeftANCValueReply(long leftVal) 
getRightANCvalue()                               					getRightANCValueReply(long rightVal) 
getBatteryLevel()                                					getBatteryLevelReply(long level)  
========================================================= =====================================================
========================================================= =====================================================  
HeadPhoneCtrl.EQSettingCtrl   									 					interface EQSettingListner 
========================================================= =====================================================
getCurrentPreset()																				getCurrentEQPresetReply(String eqName, int index)
applyPresetWithoutBand(GraphicEQPreset presetType)				none
applyPresetWithBand(GraphicEQPreset preset, int[] values) none
getAllGraphicEQValues(GraphicEQPreset preset)							getEQSettingParamReply(int preset, int numOfBand, long values[]) & getEQMinMaxParam(int limitNumBands, int limitNumSettings, int limitMin, int limitMax)  
========================================================= ===================================================== 
========================================================= ===================================================== 
HeadPhoneCtrl.CalibrateCtrl																interface CalibrateListener
========================================================= =====================================================
startCalibration()																				calibrationCompleteReply(boolean success)
stopCalibration()																					calibrationCompleteReply(boolean success)
getCalibrationStatus()																		calibrationCompleteReply(boolean success)
========================================================= =====================================================
========================================================= =====================================================  
HeadPhoneCtrl.CommonCtrl																	interface CommonListner
========================================================= =====================================================
getProgrammableIndexButton()															getProgrammableIndexButtonReply(boolean noiseOrAmbient)
setProgrammableIndexButton()															none
getConfigModelNumber()																		getConfigModelNumberReply(String modelNum)
getConfigProductName()																		getConfigProductNameReply(String prodName)
getAutoOffFeature()																				getAutoOffFeatureReply(boolean autoOff)
setAutoOffFeature(boolean autoOff)												setAutoOffFeatureReply(boolean success)
getEnableVoicePrompt()																		getEnableVoicePromptReply(boolean prompt)
setEnableVoicePrompt(boolean voiceprompt)									none
getFirmwareVersion()																			getFirmwareVersionReply(int version, int minor, int major)
setEnableVoicePrompt()																		setEnableVoicePromptReply(boolean success)
========================================================= =====================================================  



  

