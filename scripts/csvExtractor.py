import os
import re
import csv
from collections import defaultdict

# Directory to analyze
results_dir = ''
results_queryParamsExp = '/run/datad/evoMaster/em-test-utils/results/queryParamsExp'


# Regex to find test case names
test_case_pattern = re.compile(r'test_(\d+)_(\w+)')

# CSV output fields
res_home = '/run/datad/evoMaster/em-test-utils/results'
queryParamsExp_csv_file = res_home + '/queryParamsExpNames.csv'
csv_columns = ['API', 'executionMode', 'fileName', 'fullTestName', 'testNameLength', 'testDescription', 'isNameRepeated', 'descriptionCount']


def extract_test_cases(file_path):
    """Extract test case names and their details from the given file."""
    test_cases = []
    with open(file_path, 'r', encoding='utf-8') as file:
        for line in file:
            match = test_case_pattern.search(line)
            if match:
                full_test_name = match.group(0)
                if "WithQueryParam" in full_test_name:
                    test_number = match.group(1)
                    test_description = match.group(2)
                    test_cases.append((full_test_name, len(full_test_name), test_description))
    return test_cases

def analyze_directory(root_dir, csv_file):
    # List to store CSV data
    csv_data = []
    
    # Dictionary to track test descriptions per file for identifying repeated names
    test_descriptions_per_file = defaultdict(list)
    
    # Traverse directory
    for subdir, _, files in os.walk(root_dir):
        # Check if we are in the right directory level containing test files
        parts = subdir.split(os.sep)
        # print(parts)
        if len(parts) >= 8:
            API = parts[7]  # API is the first directory after outputTest
            executionMode = 'wb'
            if len(parts) == 9: # catwatch
                executionMode = parts[8]  # executionMode is under action
                          
            for file_name in files:
                if file_name.endswith('Test.java'):  # Corrected condition
                    file_path = os.path.join(subdir, file_name)
                    
                    # Extract test cases from the file
                    test_cases = extract_test_cases(file_path)
                    
                    # Track test descriptions for checking duplicates
                    test_descriptions = [desc for _, _, desc in test_cases]
                    test_descriptions_per_file[file_name].extend(test_descriptions)
                    
                    # Append data for each test case found
                    for full_test_name, test_name_length, test_description in test_cases:
                        is_repeated = test_descriptions.count(test_description) > 1
                        csv_data.append({
                            'API': API,
                            'executionMode': executionMode,
                            'fileName': file_name,
                            'fullTestName': full_test_name,
                            'testNameLength': test_name_length,
                            'testDescription': test_description,
                            'isNameRepeated': is_repeated,
                            'descriptionCount': test_descriptions.count(test_description)
                        })

    # Write data to CSV
    with open(csv_file, 'w', newline='', encoding='utf-8') as csvfile:
        writer = csv.DictWriter(csvfile, fieldnames=csv_columns)
        writer.writeheader()
        writer.writerows(csv_data)

# Run the analysis
analyze_directory(results_queryParamsExp, queryParamsExp_csv_file)   # queryParamsExp

# print(f"CSV file '{csv_file}' generated successfully.")
