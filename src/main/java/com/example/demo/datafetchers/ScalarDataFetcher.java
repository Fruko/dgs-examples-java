package com.example.demo.datafetchers;

import com.example.demo.generated.DgsConstants;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.InputArgument;

import java.util.List;
import java.util.UUID;

@DgsComponent
public class ScalarDataFetcher {
    @DgsData(parentType = DgsConstants.MUTATION.TYPE_NAME, field = DgsConstants.MUTATION.ScalarAsInput)
    public List<UUID> customScalarMutation(@InputArgument("scalar") List<UUID> scalar) {
        return scalar;
    }
}
