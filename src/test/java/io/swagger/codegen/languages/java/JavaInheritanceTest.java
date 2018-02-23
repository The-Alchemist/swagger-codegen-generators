package io.swagger.codegen.languages.java;

import com.google.common.collect.Sets;
import io.swagger.codegen.CodegenModel;
import io.swagger.codegen.languages.DefaultCodegenConfig;
import io.swagger.v3.oas.models.media.ComposedSchema;
import io.swagger.v3.oas.models.media.Discriminator;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class JavaInheritanceTest {
    @SuppressWarnings("static-method")
    @Test(description = "convert a composed model with parent")
    public void javaInheritanceTest() {
        final Schema parentModel = new Schema().name("Base");

        final Schema schema = new ComposedSchema()
                .addAllOfItem(new Schema().$ref("Base"))
                .name("composed");

        final Map<String, Schema> allSchemas = new HashMap<>();
        allSchemas.put(parentModel.getName(), parentModel);
        allSchemas.put(schema.getName(), schema);

        final DefaultCodegenConfig codegen = new JavaClientCodegen();
        final CodegenModel cm = codegen.fromModel("sample", schema, allSchemas);

        Assert.assertEquals(cm.name, "sample");
        Assert.assertEquals(cm.classname, "Sample");
        Assert.assertEquals(cm.parent, "Base");
        Assert.assertEquals(cm.imports, Sets.newHashSet("Base"));
    }


    @SuppressWarnings("static-method")
    @Test(description = "convert a composed model with parent and extra properties")
    public void javaInheritanceWithPropertiesTest() {
        final Schema parentModel = new Schema().name("Base");

        final StringSchema enumSchema = new StringSchema();
        enumSchema.setEnum(Arrays.asList("VALUE1", "VALUE2", "VALUE3"));

        final Schema extraProperty = new Schema().type("object").addProperties("name", enumSchema);


        final Schema schema = new ComposedSchema()
                .addAllOfItem(new Schema().$ref("Base"))
                .addAllOfItem(extraProperty)
                .name("composed");

        final Map<String, Schema> allSchemas = new HashMap<>();
        allSchemas.put(parentModel.getName(), parentModel);
        allSchemas.put(schema.getName(), schema);

        final DefaultCodegenConfig codegen = new JavaClientCodegen();
        final CodegenModel cm = codegen.fromModel("sample", schema, allSchemas);

        Assert.assertEquals(cm.name, "sample");
        Assert.assertEquals(cm.classname, "Sample");
        Assert.assertEquals(cm.parent, "Base");
        Assert.assertEquals(cm.imports, Sets.newHashSet("Base"));
    }


    @SuppressWarnings("static-method")
    @Test(description = "convert a composed model with discriminator")
    public void javaInheritanceWithDiscriminatorTest() {
        final Schema base = new Schema().name("Base");
        base.setDiscriminator(new Discriminator().mapping("name", StringUtils.EMPTY));

        final Schema schema = new ComposedSchema()
                .addAllOfItem(new Schema().$ref("Base"));

        final Map<String, Schema> allDefinitions = new HashMap<String, Schema>();
        allDefinitions.put("Base", base);

        final DefaultCodegenConfig codegen = new JavaClientCodegen();
        final CodegenModel cm = codegen.fromModel("sample", schema, allDefinitions);

        Assert.assertEquals(cm.name, "sample");
        Assert.assertEquals(cm.classname, "Sample");
        Assert.assertEquals(cm.parent, "Base");
        Assert.assertEquals(cm.imports, Sets.newHashSet("Base"));
    }
}
