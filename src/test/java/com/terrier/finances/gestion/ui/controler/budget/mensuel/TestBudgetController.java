package com.terrier.finances.gestion.ui.controler.budget.mensuel;

import com.terrier.finances.gestion.communs.operations.model.v12.LigneOperation;
import com.terrier.finances.gestion.ui.budget.ui.BudgetMensuelController;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Classe de test du controleur
 */
public class TestBudgetController {


    @Test
    public void testOperationComparatorOrder(){

        // Préparation
        List<LigneOperation> operationList = new ArrayList<>();
        LigneOperation l2 = new LigneOperation();
        l2.setId("ligne");
        l2.setLibelle("Ligne2");
        l2.setAutresInfos(l2.new AddInfos());
        l2.getAutresInfos().setDateOperation(LocalDateTime.of(2020, 5, 21, 12, 0));
        operationList.add(l2);

        LigneOperation l4 = new LigneOperation();
        l4.setId("postligne");
        l4.setLibelle("Ligne4");
        operationList.add(l4);

        LigneOperation l1 = new LigneOperation();
        l1.setId("ligne");
        l1.setLibelle("Ligne1");
        l1.setAutresInfos(l1.new AddInfos());
        l1.getAutresInfos().setDateOperation(LocalDateTime.of(2020, 5, 20, 12, 0));
        operationList.add(l1);

        LigneOperation l3 = new LigneOperation();
        l3.setId("postligne");
        l3.setLibelle("Ligne3");
        l3.setAutresInfos(l3.new AddInfos());
        l3.getAutresInfos().setDateOperation(LocalDateTime.of(2020, 5, 21, 12, 0));
        operationList.add(l3);

        assertEquals(l2, operationList.get(0));
        assertEquals(l4, operationList.get(1));
        assertEquals(l1, operationList.get(2));
        assertEquals(l3, operationList.get(3));

        List<LigneOperation> ordredList = BudgetMensuelController.orderOperationsToView(operationList);
        assertEquals(l4, ordredList.get(0));
        assertEquals(l2, ordredList.get(1));
        assertEquals(l3, ordredList.get(2));
        assertEquals(l1, ordredList.get(3));

    }
}
