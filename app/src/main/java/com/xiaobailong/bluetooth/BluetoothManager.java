package com.xiaobailong.bluetooth;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class BluetoothManager {

//    private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter
//            .getDefaultAdapter();

//    private final String Uuid = "00001101-0000-1000-8000-00805F9B34FB";
//
//    private Context context = null;

//	private ArrayList<BluetoothDevice> bluetoothDeviceList = new ArrayList<BluetoothDevice>();

    private BluetoothListener blueToothListener = null;

    private Socket bluetoothSocket = null;

    private OutputStream os = null;

    private InputStream is = null;

//	private BluetoothPairReceiver bluetoothPairReceiver = null;


    private int length = 19;

    public BluetoothManager(Context context, BluetoothListener blueToothListener) {
        this.blueToothListener = blueToothListener;
    }


    public boolean isBluetoothOpened() {
//        return this.mBluetoothAdapter.isEnabled();
        return true;
    }

    public void openBluetooth() {
//        this.mBluetoothAdapter.enable();
    }

    public void closeBluetooth() {
//        this.mBluetoothAdapter.disable();
    }

//	public void getBondedDevices() {
//		Set<BluetoothDevice> pairedDevices = mBluetoothAdapter
//				.getBondedDevices();
//		if (pairedDevices.size() > 0) {
//			for (BluetoothDevice device : pairedDevices) {
//				if (isHasBluetoothDevice(device) == false) {
//					bluetoothDeviceList.add(device);
//				}
//			}
//		}
//	}

//	public void search() {
//
//		Thread t = new Thread() {
//			@Override
//			public void run() {
//				IntentFilter filter = new IntentFilter(
//						BluetoothDevice.ACTION_FOUND);
//				context.registerReceiver(mReceiver, filter);
//				filter = new IntentFilter(
//						BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
//				context.registerReceiver(mReceiver, filter);
//				mBluetoothAdapter.startDiscovery();
//			}
//		};
//		t.start();
//	}

//	private BroadcastReceiver mReceiver = new BroadcastReceiver() {
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			String action = intent.getAction();
//			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
//				BluetoothDevice device = intent
//						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//				if (device != null && isHasBluetoothDevice(device) == false) {
//					bluetoothDeviceList.add(device);
//				}
//			} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED
//					.equals(action)) {
//				// search finished
//				if (blueToothListener != null) {
//					blueToothListener.optionCallBack(
//							BluetoothListener.SearchFinished, null);
//				}
//				context.unregisterReceiver(mReceiver);
//			}
//		}
//	};

//	public ArrayList<String> getAllBluetoothName() {
//		ArrayList<String> list = new ArrayList<String>();
//		for (int i = 0; i < bluetoothDeviceList.size(); i++) {
//			list.add(bluetoothDeviceList.get(i).getName());
//		}
//		return list;
//	}

//	private boolean isHasBluetoothDevice(BluetoothDevice device) {
//		for (int i = 0; i < bluetoothDeviceList.size(); i++) {
//			if (device.getAddress().equals(
//					bluetoothDeviceList.get(i).getAddress())) {
//				return true;
//			}
//		}
//		return false;
//	}

    public void connect(int position, String psw) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
//			BluetoothDevice device = this.bluetoothDeviceList.get(position);
//			int bondState = device.getBondState();
//			if (needAutoBond && bondState != BluetoothDevice.BOND_BONDED) {
//				boolean autoBond = autoBond(device, psw);
//				if (autoBond == false) {
//					error("Bluetooth connect failed !!!");
//					return;
//				}
//			}
                    if (!isBluetoothCononected()) {
                        bluetoothSocket = new Socket("192.168.4.1", 9000);
                    }
                    if (BluetoothManager.this.blueToothListener != null) {
                        BluetoothManager.this.blueToothListener.optionCallBack(
                                BluetoothListener.BluetoothConnected, null);
                    }
                } catch (IOException e) {
                    error(" connect failed !!!");
                }
            }
        }).start();
    }

//	private boolean autoBond(BluetoothDevice device, String psw) {
//		bluetoothPairReceiver = new BluetoothPairReceiver();
//		bluetoothPairReceiver.setPsw(psw);
//		IntentFilter filter = new IntentFilter();
//		filter.addAction("android.bluetooth.device.action.PAIRING_REQUEST");
//		filter.addAction("android.bluetooth.device.action.PAIRING_CANCEL");
//		context.registerReceiver(bluetoothPairReceiver, filter);
//		boolean isBondSuccess = new BluetoothAutoBond().pair(device, psw);
//		if (isBondSuccess) {
//			return true;
//		} else {
//			return false;
//		}
//	}

    private void error(String error) {
        if (this.blueToothListener != null) {
            this.blueToothListener.log(error);
        }
    }

    public void sendData(final byte[] data, final int id) {
        if (!isBluetoothCononected()) {
            error(" has not connected !");
            return;
        }
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    if (os == null) {
                        os = bluetoothSocket.getOutputStream();
                    }
                    os.write(data);
                    os.flush();
                    Thread.sleep(50);
                    if (is == null) {
                        is = bluetoothSocket.getInputStream();
                    }
                    //缓存空间，用以从外部接收数据
                    byte[] isData = new byte[length];
                    byte[] result = new byte[length];
                    int readDataLength = 0;
                    int index = 0;
                    boolean endRead = true;
                    while (endRead && (readDataLength = is.read(isData)) > 0) {
                        for (int i = 0; i < readDataLength; i++) {
                            result[index] = isData[i];
                            index++;
                            if (index == length) {
                                endRead = false;
                                break;
                            }
                        }
                    }
                    if (blueToothListener != null) {
                        blueToothListener.inputData(result, index--, id);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    error("SendData failed !!!");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    error("SendData failed !!!");
                }
            }
        };

        t.start();
    }

    private void closeIO() throws IOException {
        if (os != null) {
            os.close();
            os = null;
        }
        if (is != null) {
            is.close();
            is = null;
        }
    }

    public void close() {
        try {
            closeIO();
            if (bluetoothSocket != null) {
                bluetoothSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public boolean isBluetoothCononected() {
        if (bluetoothSocket != null) {
            return bluetoothSocket.isConnected();
        }
        return false;
    }
}
