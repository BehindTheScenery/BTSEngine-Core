package dev.behindthescenery.core.utils.code_generator;

public enum CodeGeneratorStep {
    Imports,            //import some;
    Fields,             //const var element = 10;
    Method,             //void main() {}
    MethodBody,         //method(){ ... }
    AttachToVariable    //variable_a = ...;
}
