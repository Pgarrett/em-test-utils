import sys
import csv
from collections import defaultdict
import matplotlib.pyplot as plt
import numpy as np

def parse_csv(file_path):
    """Parse a CSV file and return the parsed data as a list of dictionaries."""
    data = []
    with open(file_path, mode='r') as file:
        reader = csv.DictReader(file)
        for row in reader:
            data.append(row)
    return data

def count_execution_modes_per_api_name(data):
    """Count the number of rows for each API and executionMode."""
    api_counts = defaultdict(lambda: {'bb': 0, 'wb': 0})
    
    for row in data:
        api = row['API']
        execution_mode = row['executionMode']
        if execution_mode in ['bb', 'wb']:
            api_counts[api][execution_mode] += 1
    
    return api_counts

def count_execution_mode_by_api_type(data):
    """Count the occurrences of each executionMode per apiType."""
    execution_mode_counts = defaultdict(lambda: defaultdict(int))
    
    for row in data:
        api_type = row['apiType']
        execution_mode = row['executionMode']
        
        execution_mode_counts[api_type][execution_mode] += 1
    
    return execution_mode_counts

def format_output(file_name, api_counts, api_type_counts):
    """Format the output to display counts per API and executionMode."""
    output = f"{file_name}:\n"
    output += "Per api:\n"
    for api, counts in api_counts.items():
        output += f"- {api}:\n"
        output += f"* bb: {counts['bb']}\n"
        output += f"* wb: {counts['wb']}\n"

    output += "Total tests per api type:\n"
    for api_type, counts in api_type_counts.items():
        # for api, counts in api_counts.items():
        output += f"- {api_type}:\n"
        output += f"* bb: {counts['bb']}\n"
        output += f"* wb: {counts['wb']}\n"

    # print(api_type_counts)
    return output

def process_csv_file(file_path):
    """Process a CSV file and return formatted output."""
    data = parse_csv(file_path)
    api_counts = count_execution_modes_per_api_name(data)
    api_type_counts = count_execution_mode_by_api_type(data)
    return format_output(file_path, api_counts, api_type_counts)

def group_by_api_and_description_by_type(data):
    """Group data by apiType and then by API, fileName, and testDescription, summing up the descriptionCount without duplicates."""
    grouped_data_by_type = defaultdict(lambda: defaultdict(lambda: defaultdict(int)))
    seen_rows = set()  # To track unique rows by API:fileName:testDescription
    
    for row in data:
        execution_mode = row['executionMode']
        api = row['API']
        file_name = row['fileName']
        test_description = row['testDescription']
        unique_key = (api, execution_mode, file_name, test_description)
        
        if unique_key not in seen_rows:
            descCount = int(row['descriptionCount'])
            grouped_data_by_type[execution_mode][api][(file_name, test_description)] += descCount
            # if api == "catwatch" and execution_mode == "wb":
            #     print(f"For API: {api} of execution_mode: {execution_mode}. For({file_name},{test_description}) there are  {grouped_data_by_type[execution_mode][api][(file_name, test_description)]} tests")
            seen_rows.add(unique_key)
    
    return grouped_data_by_type

def print_api_descriptions_with_count(grouped_data_by_type):
    """Print the total descriptionCount for each unique API:fileName:testDescription combination."""
    for api_type, apis in grouped_data_by_type.items():
        print(f"API Type: {api_type}")
        for api, descriptions in apis.items():
            counts = []
            sumCounts = 0
            for (file_name, test_description), description_count in sorted(descriptions.items()):
                if description_count > 1:
                    counts.append(description_count)
                sumCounts += description_count

            print(f"\t{api}:")
            print(f"\t\tRepeated list: {counts}")
            print(f"\t\tTotal tests: {sumCounts}")
            print(f"\t\tMax repeated: {max(counts) if len(counts) > 0 else 0}\n\n\n")

def print_api_descriptions_with_count_per_api_type(grouped_data_by_type):
    """Print the total descriptionCount for each unique API:fileName:testDescription combination."""
    for execution_mode, apis in grouped_data_by_type.items():
        print(f"\nExecution Mode: {execution_mode}")
        counts = []
        sumCounts = 0
        for api, descriptions in apis.items():
            perApiCounts = []
            perApiSumCounts = 0
            for (file_name, test_description), description_count in sorted(descriptions.items()):
                if description_count > 1:
                    counts.append(description_count)
                    perApiCounts.append(description_count)
                sumCounts += description_count
                perApiSumCounts += description_count

            print(f"\t{api}:")
            print(f"\t\tTotal tests: {perApiSumCounts}")
            print(f"\t\tTotal repeated tests: {sum(perApiCounts)}")
            print(f"\t\tPercentage of repeated tests: {(sum(perApiCounts)*100)/perApiSumCounts}")
        
        # print(f"\t\tRepeated list: {counts}")
        print(f"\n\tTotal tests: {sumCounts}")
        print(f"\tTotal repeated tests: {sum(counts)}")
        print(f"\tPercentage of repeated tests: {(sum(counts)*100)/sumCounts}")
        # print(f"\t\tMax repeated: {max(counts) if len(counts) > 0 else 0}\n")

def process_and_print(file_path):
    """Process the CSV file and print the total descriptionCount for each unique combination."""
    data = parse_csv(file_path)
    grouped_data_by_type = group_by_api_and_description_by_type(data)
    print(f"\n\nProcessing file: {file_path}")
    print_api_descriptions_with_count_per_api_type(grouped_data_by_type)

    # print("\nPrint legacy values:")
    # print_api_descriptions_with_count(grouped_data_by_type)

# Example usage
path_to_files = '/Users/pgarrett/Documents/facultad/tesis/em-thesis-utils/results'
disamb_path = path_to_files + '/disambiguatedNames.csv'
amb_path = path_to_files + '/ambiguousNames.csv'
graphql_path = path_to_files + '/graphqlNames.csv'
rpc_path = path_to_files + '/rpcNames.csv'

if not len(sys.argv) > 1:
    print("Wrong usage, missing parameters")
    exit(-1)

args = sys.argv
if args[1] == "testQty":
    disamb_output = process_csv_file(disamb_path)
    amb_output = process_csv_file(amb_path)
    print(disamb_output)
    print(amb_output)
elif args[1] == "printDuplicates":
    # process_and_print(graphql_path)
    # process_and_print(rpc_path)
    process_and_print(amb_path)
    process_and_print(disamb_path)
else:
    print("Unrecognized option")
    exit(-1)
