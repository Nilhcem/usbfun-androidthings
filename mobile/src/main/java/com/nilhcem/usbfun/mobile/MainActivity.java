package com.nilhcem.usbfun.mobile;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.felhr.usbserial.UsbSerialDevice;
import com.felhr.usbserial.UsbSerialInterface;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.flexbox.JustifyContent;

import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int USB_VENDOR_ID = 0x2341; // 9025
    private static final int USB_PRODUCT_ID = 0x0001;

    private UsbManager usbManager;
    private UsbDeviceConnection connection;
    private UsbSerialDevice serialDevice;
    private String buffer = "";
    private Toast toast;

    private UsbSerialInterface.UsbReadCallback callback = new UsbSerialInterface.UsbReadCallback() {
        @Override
        public void onReceivedData(byte[] data) {
            try {
                String dataUtf8 = new String(data, "UTF-8");
                Log.i(TAG, "Data received: " + dataUtf8);

                buffer += dataUtf8;

                int index;
                while ((index = buffer.indexOf('\n')) != -1) {
                    String dataStr = buffer.substring(0, index + 1).trim();
                    buffer = buffer.length() == index ? "" : buffer.substring(index + 1);
                    toast("Data received: " + dataStr);
                }
            } catch (UnsupportedEncodingException e) {
                Log.e(TAG, "Error receiving USB data", e);
            }
        }
    };

    private View.OnClickListener onButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            UsbSerialDevice device = serialDevice;
            if (device != null) {
                int id = (int) v.getTag();
                Log.i(TAG, "onButtonClicked: #" + id);
                device.write(new byte[]{(byte) id});
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        initLayout();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startUsbConnection();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopUsbConnection();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        usbManager = null;
    }

    private void initLayout() {
        FlexboxLayout layout = new FlexboxLayout(this);
        layout.setFlexWrap(FlexWrap.WRAP);
        layout.setJustifyContent(JustifyContent.SPACE_AROUND);

        for (int i = 0; i < 12; i++) {
            Button button = new Button(this);
            button.setTag(i);
            button.setLayoutParams(new FlexboxLayout.LayoutParams(FlexboxLayout.LayoutParams.WRAP_CONTENT, FlexboxLayout.LayoutParams.WRAP_CONTENT));
            button.setOnClickListener(onButtonClicked);
            button.setText(String.format(Locale.US, "%02d", i));
            layout.addView(button);
        }
        setContentView(layout);
    }

    private void startUsbConnection() {
        Map<String, UsbDevice> usbDevices = usbManager.getDeviceList();

        if (!usbDevices.isEmpty()) {
            UsbDevice device;
            for (Map.Entry<String, UsbDevice> entry : usbDevices.entrySet()) {
                device = entry.getValue();
                if (device.getVendorId() == USB_VENDOR_ID && device.getProductId() == USB_PRODUCT_ID) {
                    Log.i(TAG, "Device found");
                    if (usbManager.hasPermission(device)) {
                        startSerialConnection(device);
                    } else {
                        Log.e(TAG, "Permission denied");
                    }
                    return;
                }
            }
        }

        Log.w(TAG, "Could not start USB connection - No devices found");
    }

    private void startSerialConnection(UsbDevice device) {
        Log.i(TAG, "Ready to open USB device connection");
        connection = usbManager.openDevice(device);
        serialDevice = UsbSerialDevice.createUsbSerialDevice(device, connection);
        if (serialDevice != null) {
            if (serialDevice.open()) {
                serialDevice.setBaudRate(115200);
                serialDevice.setDataBits(UsbSerialInterface.DATA_BITS_8);
                serialDevice.setStopBits(UsbSerialInterface.STOP_BITS_1);
                serialDevice.setParity(UsbSerialInterface.PARITY_NONE);
                serialDevice.setFlowControl(UsbSerialInterface.FLOW_CONTROL_OFF);
                serialDevice.read(callback);
                toast("Serial connection opened");
                Log.i(TAG, "Serial connection opened");
            } else {
                Log.w(TAG, "Cannot open serial connection");
            }
        } else {
            Log.w(TAG, "Could not create Usb Serial Device");
        }
    }

    private void stopUsbConnection() {
        try {
            if (serialDevice != null) {
                serialDevice.close();
            }

            if (connection != null) {
                connection.close();
            }
        } finally {
            serialDevice = null;
            connection = null;
        }
    }

    private void toast(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (toast != null) {
                    toast.cancel();
                    toast = null;
                }
                toast = Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }
}
