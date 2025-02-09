import os
import re
import csv
from collections import defaultdict

# Directory to analyze
results_dir = ''
results_queryParamsExp = '/Users/pgarrett/Documents/facultad/tesis/em-thesis-utils/results/queryParamsExp'


# Regex to find test case names
test_case_pattern = re.compile(r'test_(\d+)_(\w+)')

# File extensions corresponding to each technology
file_extensions = {
    'java': 'java',
    'js': 'js',
    'python': 'py'
}

# API type mappings
api_type_mapping = {
    'rest': ['session-service', 'features-service', 'rest-news', 'rest-ncs', 'rest-scs', 'catwatch'],
    'rpc': ['rpc-ncs', 'thrift-scs', 'rpc-scs', 'thrift-ncs'],
    'graphql': ['graphql-scs', 'graphql-ncs', 'petclinic-graphql']
}

# CSV output fields
res_home = '/Users/pgarrett/Documents/facultad/tesis/em-thesis-utils/results'
queryParamsExp_csv_file = res_home + '/queryParamsExpNames.csv'
csv_columns = ['API', 'executionMode', 'fileName', 'fullTestName', 'testNameLength', 'testDescription', 'isNameRepeated', 'descriptionCount']

def get_api_type(api):
    """Determine the API type based on the API name."""
    for api_type, apis in api_type_mapping.items():
        if api in apis:
            return api_type
    return 'unknown'

def extract_test_cases(file_path, technology):
    """Extract test case names and their details from the given file."""
    test_cases = []
    with open(file_path, 'r', encoding='utf-8') as file:
        for line in file:
            match = test_case_pattern.search(line)
            if match:
                full_test_name = match.group(0)
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
        if len(parts) >= 13:
            API = parts[9]  # API is the first directory after outputTest
            executionMode = parts[11]  # executionMode is under action
            technology = parts[12]  # technology is the last directory level
            
            # Get the API type
            api_type = get_api_type(API)
            
            # Filter test files for the current technology
            ext = file_extensions.get(technology)
            if ext:
                for file_name in files:
                    if file_name.endswith(f'Test.{ext}'):  # Corrected condition
                        file_path = os.path.join(subdir, file_name)
                        
                        # Extract test cases from the file
                        test_cases = extract_test_cases(file_path, technology)
                        
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
