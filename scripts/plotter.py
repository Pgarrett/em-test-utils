import matplotlib.pyplot as plt
import argparse

perc_per_api = {
    "catwatch": [56, 51, 90, 79],
    "scs": [43, 29, 89, 88],
    "session-service": [88, 87, 96, 93],
    "features-service": [31, 6, 27, 23],
    "news": [50, 27, 55, 49],
    "ncs": [63, 56, 74, 73]
}

def plot_repeated_names_percentage(api_name):
    """
    Generate a bar plot with hardcoded values representing the percentage of repeated names.
    """
    # Hardcoded values for the bar heights (these are percentages)
    # values = [20, 35, 50, 40]  # Replace these with your specific percentages if needed
    values = perc_per_api[api_name]

    # Labels for the legend
    labels = ['bb ambiguo', 'bb desambiguado', 'wb ambiguo', 'wb desambiguado']

    # Colors for each bar
    colors = ['#1f77b4', '#ff7f0e', '#2ca02c', '#d62728']  # Blue, Orange, Green, Red

    # Create a figure and axis
    fig, ax = plt.subplots()

    # X positions for the bars
    x_positions = range(len(labels))

    # Create bars
    bars = ax.bar(x_positions, values, color=colors)

    # Set x-axis label
    ax.set_xlabel('Tipo de ejecución')

    # Set y-axis label
    ax.set_ylabel('% nombres repetidos')

    # Set title
    # ax.set_title('Porcentaje de Nombres Repetidos por Tipo de Ejecución')
    ax.set_title(f'Ejecución API {api_name}')

    # Add legend
    ax.legend(bars, labels)

    # Set x-axis ticks (empty in this case as requested)
    ax.set_xticks([])

    # Display the plot
    plt.show()

def main():
    """
    Main function to execute the appropriate plotting function based on the provided argument.
    """
    parser = argparse.ArgumentParser(description="Execute specific plotting functions.")
    
    # Define available functions
    parser.add_argument(
        'function', 
        type=str, 
        choices=['perc_rep'],
        help="Function to execute. Options: 'plot_repeated_names_percentage'"
    )
    
    args = parser.parse_args()
    
    # Execute the corresponding function
    if args.function == 'perc_rep':
        plot_repeated_names_percentage('catwatch')
        plot_repeated_names_percentage('scs')
        plot_repeated_names_percentage('session-service')
        plot_repeated_names_percentage('features-service')
        plot_repeated_names_percentage('news')
        plot_repeated_names_percentage('ncs')

if __name__ == '__main__':
    main()
