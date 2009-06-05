

// Include your fully-qualified package statement.
package org.vuphone.wwatch.android;

// Interface for all calls that anyone might
// want to make on the settings view
interface ISettingsViewCallback {
    
    void accelerometerChanged(in float x, in float y, in float z);
    
    void gpsChanged(in double lat, in double lng);
    
    void setRealSpeed(in double speed);
    
    void setScaleSpeed(in double speed);
    
    void setAccelerometerMultiplier(in float multip);
    
}