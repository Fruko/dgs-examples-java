package com.example.demo.datafetchers;

import com.example.demo.generated.DgsConstants;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.InputArgument;
import graphql.schema.DataFetchingEnvironment;

import java.util.List;
import java.util.UUID;

@DgsComponent
public class ScalarDataFetcher {
    @DgsData(parentType = DgsConstants.MUTATION.TYPE_NAME, field = DgsConstants.MUTATION.ScalarAsInput)
    public List<UUID> customScalarMutation(@InputArgument("scalar") List<UUID> scalar,
                                           DataFetchingEnvironment dataFetchingEnvironment) throws Exception {
        if(null != scalar.get(0) && scalar.get(0) instanceof UUID) {
            return scalar;
        } else {
            throw new Exception("Not UUID");
        }
    }

    @DgsData(parentType = DgsConstants.MUTATION.TYPE_NAME, field = "intAsInput")
    public int intAsInput(@InputArgument("number") int number,
                                           DataFetchingEnvironment dataFetchingEnvironment) throws Exception {
        return number;
    }
}
