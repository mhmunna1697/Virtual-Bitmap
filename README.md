# Implementation of Multi-Flow-Spread Sketche

This project implements the **Virtual Bitmap** algorithm to estimate the spread (number of distinct elements) of network flows using a compact probabilistic data structure.

## 📘 Overview

Virtual Bitmap is a technique used for **network measurement**, specifically for estimating the number of distinct elements (e.g., destination IPs) associated with a flow. It uses a large shared physical bitmap and assigns each flow a virtual bitmap mapped via hash functions.

This method offers a good trade-off between **accuracy and memory efficiency**, especially in environments with a large number of flows.

## 🛠️ How It Works

- Each flow is assigned a virtual bitmap of size `l` bits.
- All virtual bits are mapped into a shared physical bitmap of size `m`.
- When recording an element of a flow, one of the virtual bits is selected randomly and its corresponding physical bit is set to 1.
- After processing all flows, the fraction of zero bits in the virtual bitmap (`Vf`) and in the global physical bitmap (`Vm`) is used to estimate the number of distinct elements per flow.

### Estimation Formula

Let:

- `l` = number of virtual bits  
- `m` = number of physical bits  
- `Vf` = fraction of zero bits in a flow’s virtual bitmap  
- `Vm` = fraction of zero bits in the physical bitmap  

Then the estimated spread for a flow is:

Estimated = l * ln(Vm / Vf)


## 📥 Input

The input file `project5input.txt` contains flow data.  
The first line is the number of flows (`n`).  
Each subsequent line contains a flow ID and its true spread (tab-separated).

**Example input:**
8507
49.77.20.20 9
185.35.62.200 205
88.100.184.82 20
200.158.26.238 5
...


## ▶️ Execution

The Java program performs the following:

1. Initializes a physical bitmap of size `m = 500000`
2. Assigns each flow a virtual bitmap of size `l = 500`
3. Records each element of all flows by mapping virtual bits to physical bits
4. Estimates the spread using the estimation formula
5. Writes the true and estimated spreads to `virtual_bitmap_output.txt`

## 📊 Output

- `virtual_bitmap_output.txt`: Contains two numbers per line – the true spread and the estimated spread.
- A Python script (`plot_virtual_bitmap.py`) is provided to generate a figure of true vs. estimated spread and save it as a PDF.

**Sample Output:**

9 8
205 211
20 20
5 5
...
