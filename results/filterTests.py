import csv

def filter_csv(input_file, output_file, keyword):
    with open(input_file, mode='r', newline='', encoding='utf-8') as infile:
        reader = csv.DictReader(infile)
        rows_to_keep = [row for row in reader if keyword in row['testDescription']]

    with open(output_file, mode='w', newline='', encoding='utf-8') as outfile:
        writer = csv.DictWriter(outfile, fieldnames=reader.fieldnames)
        writer.writeheader()
        writer.writerows(rows_to_keep)

# Usage
input_csv = 'queryParamsExpNames.csv'  # Replace with your input CSV file path
output_csv = 'filteredQPNames.csv'  # Replace with your desired output CSV file path
filter_csv(input_csv, output_csv, 'WithQueryParam')
