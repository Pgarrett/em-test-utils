// curent (06/10/2024) action printing code. Use in TestCaseWriter.convertToCompilableTestCode just before the `@Test` annotations
lines.addSingleCommentLine("Individual:")
lines.addSingleCommentLine("\tActions:")
test.test.individual.seeAllActions().forEach { ac ->
    lines.addSingleCommentLine("\t\t${ac.javaClass.kotlin.qualifiedName}: ${ac.getName()}")
    lines.addSingleCommentLine("\t\t\tAction parameters:")
    if (ac is ApiWsAction) {
        ac.parameters.forEach { acParam ->
            lines.addSingleCommentLine(
                "\t\t\t\t${acParam.name}: '${
                    acParam.primaryGene().getValueAsRawString()
                }'"
            )
        }
    } else if (ac is SqlAction) {
        lines.addSingleCommentLine("\t\t\t\t${ac.getResolvedName()}")
    }
    lines.addSingleCommentLine("\t\t\tGenes:")
    ac.seeTopGenes().forEach { gene ->
        try {
            lines.addSingleCommentLine("\t\t\t\t${gene.javaClass.kotlin.qualifiedName} = ${gene.getVariableName()}:'${gene.getValueAsRawString()}'")
        } catch (e: Exception) {
            lines.appendSingleCommentLine("\t\t\t\tError trying to print ${gene.javaClass.kotlin.qualifiedName} information: ${e.localizedMessage}")
        }
    }
}
lines.addSingleCommentLine("\tEvaluated Actions:")
test.test.evaluatedMainActions().forEach { eAc ->
    lines.addSingleCommentLine("\t\t${eAc.action.javaClass.kotlin.qualifiedName}: ${eAc.action.getName()}")
}








// original gene printing code

if (config.outputFormat.isPython() || config.outputFormat.isJava()) {
    solution.individuals.forEach { eInd ->
        lines.addSingleCommentLine("Individual:")
        eInd.individual.seeAllActions().forEach { ac ->
            lines.addSingleCommentLine("\t${ac.javaClass.kotlin.qualifiedName}: ${ac.getName()}")
            ac.seeTopGenes().forEach { gene ->
                lines.addSingleCommentLine("\t\t${gene.javaClass.kotlin.qualifiedName} = ${gene.getVariableName()}:${gene.getValueAsRawString()}")
            }
        }
    }
}


if (config.outputFormat.isPython() || config.outputFormat.isJava()) {
    solution.individuals.forEach { eInd ->
        lines.addSingleCommentLine("Individual:")
        lines.addSingleCommentLine("\tActions:")
        eInd.individual.seeAllActions().forEach { ac ->
            lines.addSingleCommentLine("\t\t${ac.javaClass.kotlin.qualifiedName}: ${ac.getName()}")
            ac.seeTopGenes().forEach { gene ->
                lines.addSingleCommentLine("\t\t\t${gene.javaClass.kotlin.qualifiedName} = ${gene.getVariableName()}:${gene.getValueAsRawString()}")
            }
        }
        lines.addSingleCommentLine("\tEvaluated Actions:")
        eInd.evaluatedMainActions().forEach { eAc ->
            lines.addSingleCommentLine("\t\t${eAc.action.javaClass.kotlin.qualifiedName}: ${eAc.action.getName()}")
            lines.addSingleCommentLine("\t\tResult: ${(eAc.result as RestCallResult).getStatusCode()}")
        }
    }
}