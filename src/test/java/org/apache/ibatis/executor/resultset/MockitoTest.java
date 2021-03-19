package org.apache.ibatis.executor.resultset;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.spy;

/**
 * @author heqin
 */
@ExtendWith(MockitoExtension.class)
public class MockitoTest {

    @Mock
    private List<String> mockList;

    private List<String> mockList_spy = spy(ArrayList.class);

    @Test
    public void add_mockList_thenNoAffect() {
        //当在mockList中添加一个元素后，没有影响
        mockList.add("xiaoming");
        assertEquals(0, mockList.size());
    }

    @Test
    public void add_spyMockList_thenAffect() {
        //当在mockList_spy中添加一个元素后，有影响
        mockList_spy.add("xiaoming");
        assertEquals(1, mockList_spy.size());
    }
}
