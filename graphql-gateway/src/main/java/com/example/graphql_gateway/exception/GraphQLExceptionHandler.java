package com.example.graphql_gateway.exception;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.stereotype.Component;

@Component
public class GraphQLExceptionHandler extends DataFetcherExceptionResolverAdapter {

    @Override
    protected GraphQLError resolveToSingleError(
            Throwable ex,
            DataFetchingEnvironment env
    ) {

        if (ex instanceof ForbiddenException) {
            return GraphqlErrorBuilder.newError()
                    .message(ex.getMessage())
                    .errorType(ErrorType.FORBIDDEN)
                    .build();
        }

        if (ex instanceof ResourceNotFoundException) {
            return GraphqlErrorBuilder.newError()
                    .message(ex.getMessage())
                    .errorType(ErrorType.NOT_FOUND)
                    .build();
        }

        return GraphqlErrorBuilder.newError()
                .message("Internal server error")
                .errorType(ErrorType.INTERNAL_ERROR)
                .build();
    }
}
