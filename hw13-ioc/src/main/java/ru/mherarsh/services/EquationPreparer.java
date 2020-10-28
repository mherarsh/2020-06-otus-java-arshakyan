package ru.mherarsh.services;

import ru.mherarsh.model.Equation;

import java.util.List;

public interface EquationPreparer {
    List<Equation> prepareEquationsFor(int base);
}
