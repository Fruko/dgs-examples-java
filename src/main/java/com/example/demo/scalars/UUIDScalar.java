package com.example.demo.scalars;

import com.netflix.graphql.dgs.DgsScalar;
import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;

import java.util.UUID;

@DgsScalar(name="UUID")
public class UUIDScalar implements Coercing<UUID, String> {
    @Override
    public String serialize(Object dataFetcherResult) throws CoercingSerializeException {
        if (dataFetcherResult instanceof UUID) {
            return dataFetcherResult.toString();
        } else {
            throw new CoercingSerializeException("Not a valid UUID");
        }
    }

    @Override
    public UUID parseValue(Object input) throws CoercingParseValueException {
        return UUID.fromString(input.toString());
    }

    @Override
    public UUID parseLiteral(Object input) throws CoercingParseLiteralException {
        UUID result = UUID.fromString(((StringValue) input).getValue());
        if (input instanceof StringValue) {
            return UUID.fromString(((StringValue) input).getValue());
        }

        throw new CoercingParseLiteralException("Value is not a valid UUID");
    }
}
