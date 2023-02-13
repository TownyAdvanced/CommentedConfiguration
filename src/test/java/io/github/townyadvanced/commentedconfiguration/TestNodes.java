package io.github.townyadvanced.commentedconfiguration;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;
import io.github.townyadvanced.commentedconfiguration.setting.CommentedNode;
import io.github.townyadvanced.commentedconfiguration.setting.SimpleNode;
import io.github.townyadvanced.commentedconfiguration.setting.TypedValueNode;
import org.bukkit.Location;

public class TestNodes {
    private static final List<CommentedNode> nodes = new ArrayList<>();

    private static <T> TypedValueNode<T> node(TypedValueNode<T> node) {
        nodes.add(node);
        return node;
    }

    private static final TypedValueNode<Object> HEADER_NODE = node(SimpleNode.builder("test", Object.class)
            .comment("This config is for testing")
            .comment("# It is not intended for use in production")
            .build());

    public static final TypedValueNode<Boolean> BOOLEAN_NODE = node(SimpleNode.builder("test.boolean", Boolean.class)
            .defaultValue(false)
            .comment("This is a boolean")
            .build());

    public static final TypedValueNode<String> STRING_NODE = node(SimpleNode.builder("test.string", String.class)
            .defaultValue("default")
            .comment("This is a string")
            .build());

    public static final TypedValueNode<Integer> INTEGER_NODE = node(SimpleNode.builder("test.integer", Integer.class)
            .defaultValue(1234)
            .comment("This is an integer")
            .build());

    public static final TypedValueNode<Location> LOCATION_NODE = node(SimpleNode.builder("test.location", Location.class)
            .defaultValue(new Location(null, 0, 0, 0))
            .comment("This is a location")
            .build());

    public static final TypedValueNode<List> LIST_NODE = node(SimpleNode.builder("test.nested.list", List.class)
            .defaultValue(Lists.newArrayList("one", "two", "three"))
            .comment("This is a list")
            .build());

    public static List<CommentedNode> getAllNodes() {
        return nodes;
    }
}
