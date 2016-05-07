API Documentation (Android)
=============================



Class of PulseColor
---------------------------------------

This class is defined for the color input or output the device

**Signature:**

``public class PulseColor``

**Members:**

``public byte red`` - the red color

``public byte green;`` - the green color

``public byte blue`` - the blue color




Class of DeviceModel
---------------------------------------

This class is defined for the information of the device in link system

**Signature:**

``public class DeviceModel``

**Members:**

``public int DeviceIndex`` - the index of device in link system

``public String DeviceName`` - the device name

``public String Product`` - the product name

``public String Module`` - the module name

``public int BatteryPower`` - the battery level

``public int LinkedDeviceCount`` - the device count of linked with it

``public int ActiveChannel`` - the channel of device

``public int AudioSource`` - the audio source

``public String Mac`` - the mac address




Enum of Pattern
---------------------------------------

This enum is defined for pattern style of the device

**Signature:**

``public enum PulseThemePattern``

**Members:**

``PulseTheme_Firework`` - the firework style

``PulseTheme_Traffic`` - the traffic style

``PulseTheme_Star`` - the star style

``PulseTheme_Wave`` - the wave style

``PulseTheme_Firefly`` - the firefly style

``PulseTheme_Rain`` - the rain style

``PulseTheme_Fire`` - the fire  style

``PulseTheme_Canvas`` - the canvas style

``PulseTheme_Hourglass`` - the hourglass style

``PulseTheme_Ripple`` - the ripple style



APIs for PulseHandler
---------------------------------------


registerPulseNotifiedListener
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Register the listener of pulse notify

**Signature:**

``void registerPulseNotifiedListener(PulseNotifiedListener listener)``

**Parameters:**

``void``

**Returns:**

``void``


ConnectMasterDevice
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Connect to master speaker

**Signature:**

``boolean ConnectMasterDevice()``

**Parameters:**

``void``

**Returns:**

``boolean`` - success or fail


isConnectMasterDevice
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Connect to master speaker success or fail

**Signature:**

``boolean isConnectMasterDevice()``

**Parameters:**

``void``

**Returns:**

``boolean`` - success or fail


RequestDeviceInfo
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Request the information of all speakers in link system

**Signature:**

``boolean RequestDeviceInfo()``

**Parameters:**

``void``

**Returns:**

``boolean`` - success or fail


SetDeviceName
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Change Device name

**Signature:**

boolean SetDeviceName(String devName, int devIndex)``

**Parameters:**

``String devName`` - device name

``int devIndex`` - device index

**Returns:**

``boolean`` - success or fail

SetDeviceChannel
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Set product of device ID

**Signature:**

``boolean SetDeviceChannel(int devIndex, int channel)``

**Parameters:**

``int devIndex`` - device index

``int channel`` - device channel

**Returns:**

``boolean`` - success or fail


SetLEDPattern
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Change device LED pattern information

**Signature:**

``boolean SetLEDPattern(PulseThemePattern pattern)``

**Parameters:**

``PulseThemePattern pattern`` - LED pattern

**Returns:**

``boolean`` - success or fail


GetLEDPattern
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Get LED pattern information

**Signature:**

``boolean GetLEDPattern()``

**Parameters:**

``void``

**Returns:**

``boolean`` - success or fail


SetBrightness
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Change device LED brightness.

**Signature:**

``boolean SetBrightness(int brightness)``

**Parameters:**

``int brightness`` - brightness level (0 ~ 100)

**Returns:**

``boolean`` - success or fail

GetBrightness
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Get device LED brightness.

**Signature:**

``boolean SetBrightness()``

**Parameters:**

``void``

**Returns:**

``boolean`` - success or fail

SetBackgroundColor
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Set the background color of the master speaker, or the master and all other speakers (slaves) within BLE signal range.

**Signature:**

``boolean SetBackgroundColor(PulseColor color, boolean inlcudeSlave)``

**Parameters:**

``PulseColor color`` - background color

``boolean inlcudeSlave`` - include slave or not

**Returns:**

``boolean`` - success or fail


SetBackgroundColor
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

**Signature:**

``boolean SetBackgroundColor(byte red, byte green, byte blue, boolean inlcudeSlave)``

**Parameters:**

``byte red`` - red color in RGB

``byte green`` - green color in RGB

``byte blue`` - blue color in RGB

``boolean inlcudeSlave`` - include slave or not

**Returns:**

``boolean`` - success or fail


SetColorImage
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Send 9x11 color bitmap to Pulse.

**Signature:**

``boolean SetColorImage(PulseColor[] bitmap)``

**Parameters:**

``PulseColor[] bitmap`` - 9x11 color bitmap

**Returns:**

``boolean`` - success or fail


SetCharacterPattern
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Set a Character pattern to master speaker. If Including Slaves parameter is 1, all the slave speakers within BLE signal range should display the Character pattern as well. The charactors are include in ``0..9, A..Z, ?, !, $, +, -, =, %, *, /, #``.

**Signature:**

``boolean SetCharacterPattern(char charId, PulseColor foregroundColor , PulseColor backgroundColor,  boolean includeSlave)``

**Parameters:**

``int charId`` - charId ID

``PulseColor foregroundColor`` - foreground color

``PulseColor backgroundColor`` - background color

``boolean includeSlave`` - include slave or not

**Returns:**

``boolean`` - success or fail

PropagateCurrentLedPattern
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
Propagate LED pattern of master device to others

**Signature:**

``boolean PropagateCurrentLedPattern()``

**Parameters:**

``void``

**Returns:**

``boolean`` - success or fail

GetMicrophoneSoundLevel
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
Capture sound level in one second by Mic

**Signature:**

``void GetMicrophoneSoundLevel()``

**Parameters:**

``void``

**Returns:**

``void``

CaptureColorFromColorPicker
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
Capture color by Color Picker

**Signature:**

``void CaptureColorFromColorPicker()``

**Parameters:**

``void``

**Returns:**

``void``


APIs for events handling
---------------------------------------

onConnectMasterDevice
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The notify of the phone connected with pulse.

**Signature:**

``void onConnectMasterDevice()``

**Parameters:**

``void``

**Returns:**

``void``

onDisconnectMasterDevice
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The notify of the phone disconnected with pulse.

**Signature:**

``void onDisconnectMasterDevice()``

**Parameters:**

``void``

**Returns:**

``void``

onLEDPatternChanged
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

When device led pattern change, it will be notified by NotifyLedPattern

**Signature:**

``void onLEDPatternChanged(PulseThemePattern pattern)``

**Parameters:**

``PulseThemePattern`` - LED pattern

**Returns:**

``void``

onRetBrightness
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The notify of Get brightness information.

**Signature:**

``void onRetBrightness(int brightness)``

**Parameters:**

``int brightness`` - brightness value

**Returns:**

``void``

onRetSetDeviceInfo
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The result of set the information of device such as SetLEDPattern, SetDeviceChannel, SetBrightness, SetBackgroundColor, SetColorImage and SetCharacterPattern.

**Signature:**

``void onRetSetDeviceInfo(boolean ret)``

**Parameters:**

``boolean ret`` - success or fail

**Returns:**

``void``

onRetRequestDeviceInfo
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The notify of request the information of all speakers in link system.

**Signature:**

``void onRetRequestDeviceInfo(DeviceModel[] deviceModel)``

**Parameters:**

``DeviceModel[]`` - device model list. If no device connect, it return null

**Returns:**

``void``


onRetSetLEDPattern
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The notify of Get LED pattern information.

**Signature:**

``void onRetGetLEDPattern(boolean ret)``

**Parameters:**

``boolean ret`` - success or fail

**Returns:**

``void``

onRetGetLEDPattern
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The notify of Get LED pattern information.

**Signature:**

``void onRetGetLEDPattern(PulseThemePattern pattern)``

**Parameters:**

``PulseThemePattern`` - LED pattern

**Returns:**

``void``


onSoundEvent
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Get notified of Sound Event that speaker listened.

**Signature:**

``void onSoundEvent(int soundLevel)``

**Parameters:**

``int soundLevel`` - Sound level

**Returns:**

``void``

onRetCaptureColor
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The callback function of capture color by Color Picker

**Signature:**

``void onRetCaptureColor(PulseColor capturedColor)``

**Parameters:**

``PulseColor capturedColor`` - captured color

**Returns:**

``void``


onRetCaptureColor
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The callback function of capture color by Color Picker

**Signature:**

``void onRetCaptureColor(byte red, byte green, byte blue)``

**Parameters:**

``byte red`` - red color in RGB

``byte green`` - green color in RGB

``byte blue`` - blue color in RGB

**Returns:**

``void``