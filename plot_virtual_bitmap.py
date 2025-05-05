import matplotlib.pyplot as plt

# Load data
true_spreads = []
estimated_spreads = []

with open("virtual_bitmap_output.txt", "r") as file:
    for line in file:
        parts = line.strip().split()
        if len(parts) != 2:
            continue
        true, est = map(int, parts)
        true_spreads.append(true)
        estimated_spreads.append(est)

# Create plot
plt.figure(figsize=(8, 6))
plt.scatter(true_spreads, estimated_spreads, color='blue', s=10)

plt.xlabel("True Spread")
plt.ylabel("Estimated Spread")
plt.title("Virtual Bitmap: True vs Estimated Spread")
plt.grid(True)
plt.xlim(0, 500)  
plt.ylim(0, 700)

# Save as PDF
plt.tight_layout()
plt.savefig("virtual_bitmap_plot.pdf", format="pdf")

print("Plot saved as virtual_bitmap_plot.pdf")
