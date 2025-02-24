import random

def get_random_strings_from_map(string_map, total_count=10):
    """
    Returns a list of `total_count` random strings from the given map.

    - Picks one random string from each list corresponding to a key.
    - Since there are 9 keys, it selects an additional random string from a list with more than one element.
    
    :param string_map: Dictionary of `string: List[string]`
    :param total_count: Total number of strings to return (default is 10).
    :return: List of selected random strings.
    """
    
    if len(string_map) != 9:
        raise ValueError("The map must have exactly 9 keys.")

    selected_strings = []
    remaining_choices = []

    # Step 1: Pick one random string from each key's list
    for key, values in string_map.items():
        print(f"'{key}' API has {len(values)} test cases")
        choice = random.choice(values)
        selected_strings.append(choice)
        if len(values) > 1:
            remaining_choices.extend([v for v in values if v != choice])  # Keep the unchosen values

    # Step 2: Pick an additional string from the remaining choices
    if not remaining_choices:
        raise ValueError("There must be at least one list with more than one element to pick the extra string.")

    selected_strings.append(random.choice(remaining_choices))

    return selected_strings

# Example usage
if __name__ == "__main__":

    tests_per_api = {
        "catwatch": ["postOnPaymentRefundsWithQueryParamsEmptyStateShowsFaults_100_200", "getOnV1RefundsWithQueryParamsEmptyLast_digits_card_number", "getOnV1RefundsWithQueryParamsEmptyFrom_settled_dateShowsFaults_100_200", "getOnLanguagesWithQueryParamsNegativeOffsetReturnsSchemaInvalidResponse", "getOnInitWithQueryParamEmptyAccess_tokenReturnsSchemaInvalidResponse", "getOnContributorsWithQueryParamsEmptyStart_dateAndEmptySortBy", "getOnProjectsWithQueryParamsNegativeOffsetReturnsSchemaInvalidResponse", "getOnLanguagesWithQueryParamsNegativeLimitShowsFaults_100_200", "getOnV1RefundsWithQueryParamsEmptyReferenceShowsFaults_100_200", "getOnV1PaymentsWithQueryParamsEmptyFirst_digits_card_number", "getOnStatisticsProjectsWithQueryParamsEmptyOrganizations", "getOnStatisticsContributorsWithQueryParamsEmptyOrganizations", "getOnConfigWithQueryParamEmptyAccess_tokenReturnsSchemaInvalidResponse", "getOnContributorsWithQueryParamsEmptyAccess_token", "postOnConfigScoring_projectWithQueryParamEmptyAccess_token", "getOnContributorsWithQueryParamsNegativeOffsetEmptySortBy", "getOnExportWithQueryParamsEmptyOrganizationsReturnsSchemaInvalidResponse"],
        "genome-nexus": ["getOnAnnotationGenomWithQueryParamsEmptyIsoformOverrideSourceReturns404", "getOnAnnotationDbsnpWithQueryParamsEmptyIsoformOverrideSourceUsingMongo", "getOnAnnotWithQueryParamsEmptyTokenReturnsObject", "getOnAnnotationDbsnpWithQueryParamsEmptyTokenReturnsObjectUsingMongo", "postOnAnnotationWithQueryParamsEmptyTokenReturns200", "postOnAnnotationGenomicWithQueryParamEmptyIsoformOverrideSource", "postOnAnnotationGenomicWithQueryParamsEmptyIsoformOverrideSource", "postOnDbsnpDbsnpWithQueryParamsEmptyTokenReturns200UsingMongo", "postOnDbsnpDbsnpWithQueryParamsEmptyIsoformOverrideSourceReturns200"],
        "gestaohospital": ["getOnHospitaisMaisProximoWithQueryParamsNegativeLonShowsFaults_100_200", "getOnHospitaisMaisProximoWithQueryParamsNegativeRaioMaximo", "putOnHospitaiEstoquWithQueryParamsEmptyRaioMaximo", "getOnHospitaisMaisProximoWithQueryParamsNegativeLonReturnsObject"],
        "market": ["getOnCustomerWithQueryParamEmptyIncludedReturnsSchemaInvalidResponse", "getOnCustomerCartWithQueryParamEmptyNameReturnsSchemaInvalidResponse", "putOnCartDeliveryWithQueryParamIncludedReturnsSchemaInvalidResponse"],
        "pay-publicapi": ["getOnPaymentEventsWithQueryParamsEmptyFrom_dateShowsFaults_100_200", "postOnPaymentCancelWithQueryParamsEmptyPageShowsFaults_100_200", "postOnPaymentCancelWithQueryParamsEmptyCardholder_name", "postOnPaymentCancelWithQueryParamsEmptyFrom_dateShowsFaults_100_200", "getOnV1PaymentWithQueryParamsEmptyReferenceShowsFaults_100_200", "postOnPaymentCaptureWithQueryParamsEmptyDisplay_sizeShowsFaults_100_200", "postOnPaymentCaptureWithQueryParamsEmptyTo_settled_date", "postOnV1PaymentsWithQueryParamsEmptyCardholder_nameShowsFaults_100_200", "postOnV1PaymentsWithQueryParamsEmptyCard_brandShowsFaults_100_200", "getOnV1PaymentsWithQueryParamsEmptyTo_dateShowsFaults_100_200", "getOnPaymentRefundWithQueryParamsEmptyPageReturnsSchemaInvalidResponse", "postOnPaymentCaptureWithQueryParamsEmptyLast_digits_card_number"],
        "rest-news": ["getOnNewsWithQueryParamsEmptyCountryReturnsEmptyList"],
        "restcountries": ["getOnV2CapitWithQueryParamsEmptyFieldsReturnsSchemaInvalidResponse", "getOnV2AlphaWithQueryParamsEmptyFieldsReturnsSchemaInvalidResponse", "getOnV1NameWithQueryParamsFullTextReturnsSchemaInvalidResponse", "getOnV1AlphaWithQueryParamEmptyCodesReturnsSchemaInvalidResponse", "getOnV2AlphaWithQueryParamsEmptyCodesReturnsSchemaInvalidResponse", "getOnV1NameWithQueryParamFullTextReturnsSchemaInvalidResponse"],
        "scout-api": ["getOnV2ActivitiesWithQueryParamsFeaturedNegativeRandomReturns400UsingSql", "getOnV2ActivitiesWithQueryParamsFeaturedNegativeRandomReturns400", "getOnMedia_filFileWithQueryParamNegativeSize", "getOnV2ActivitiesWithQueryParamsMy_favouritesUsingSql", "getOnV1ActivitiesWithQueryParamsFeaturedAndMy_favourites", "getOnV1ActivitiesWithQueryParamsMy_favouritesNegativeFavourites", "getOnV1ActivitiesWithQueryParamsFeaturedAndMy_favouritesUsingSql", "getOnV1ActivitiesWithQueryParamsMy_favourites", "deleteOnV1Media_filWithQueryParamsVerify_unused", "getOnV1CategoriesWithQueryParamsNegativeMin_activities_count", "getOnV2TagsWithQueryParamsNegativeMin_activities_countReturnsEmptyList", "postOnV1CategoriesWithQueryParamsEmptyGroupReturns200", "getOnV2ActivitiesWithQueryParamsFeaturedNegativeRatings_count_min", "getOnV2ActivitiesWithQueryParamsNegativeFavouritesReturnsEmptyList", "getOnV2ActivitiesWithQueryParamsNegativeRatings_average_minEmptyId", "getOnV2ActivitiesWithQueryParamsFeaturedNegativeRatings_average_min", "postOnV2ActivitiesWithQueryParamsEmptyDurationsReturns200", "postOnV1FavouritesWithQueryParamsEmptyTime_2Returns204"],
        "session-service": ["postOnQueryFetchWithQueryParamsEmptyValueReturnsSchemaInvalidResponse"]
    }

    tests_to_process = 0
    for _, values in tests_per_api.items():
        tests_to_process += len(values)
    print(f"Total amount of tests to process: {tests_to_process}")

    
    random_strings = get_random_strings_from_map(tests_per_api, 10)
    print("Random strings:", random_strings)
