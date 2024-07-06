

public class LinkedLists
{
    private ListNode first;
    private ListNode last;
    private ListNode current;
    //default constructer
    public LinkedLists()
    {
        first=null;
        last=null;
        current=null;
    }
    //method to check either the list is empty
    public boolean isEmpty()
    {
        return first == null;
    }
    //method to add new element in the front of the list
    public void insertAtFront(Object insertItem)
    {
        if(isEmpty())
        {
            first = last = new ListNode(insertItem);
        }
        else 
        {
            first = new ListNode(insertItem,first);
        }
    }
    //method to add new element in the back of the list
    public void insertAtBack(Object insertItem)
    {
        if(isEmpty())
        {
            first = last = new ListNode(insertItem);
        }
        else
        {
            last = last.next = new ListNode(insertItem,last);
        }
    }

    /*
    //method to remove an element from the front of the list
    public Object removeFromFront() throws EmptyListException
    {
        Object removeItem = null;
        if(isEmpty())
        {
            throw new EmptyListException();
        }
        removeItem = (Object)first.data;
        
        if(first.equals(last))
        {
            first = last = null;
        }
        else 
        {
            first = first.next;
        }
        return removeItem;
    }
    //method to remove an element from the back of the list
    public Object removeFromBack() throws EmptyListException
    {
        Object removeItem = null;
        if(isEmpty())
        {
            throw new EmptyListException();
        }
        removeItem = (Object)last.data;
        
        if(first.equals(last))
        {
            first = last = null;
        }
        else 
        {
            ListNode current = first;
            while(current.next != last)
            {
                current = current.next;
            }
            last = current;
            current.next = null;
        }
        return removeItem;
    }
    */
    //method to return the first element in the list
    public Object getHead()
    {
        if(isEmpty())
        {
            return null;
        }
        else 
        {
            current = first;
            return (Object)current.data;
        }
    }
    //method to return the last element in the list
    public Object getNext()
    {
        if(current != last)
        {
            current = current.next;
            return (Object)current.data;
        }
        else 
        { 
            return null;
        }
    }
    //method to display all the elements in the list

    public void displayElements() 
    {
        ListNode current = first;
        while (current != null) {
            System.out.println("----------------------------------");
            System.out.println(current.data);
            current = current.next;}
    }
    //method to return the size of the list
    public int size() {
        int count = 0;
        ListNode current = first;  // Start from the head node
        
        while (current != null) {
            count++;
            current = current.next;  // Move to the next node
        }

        return count;
    }
    
    public Object removeNode(Object fObj) {
        Object removeItem = null;
        ListNode previous = null;

        if (isEmpty()) {
            throw new EmptyListException();
        }

        current = first;

        // Check if the first node is the one to be removed
        if (first.data.equals(fObj)) {
            removeItem = (Object)first.data;
            first = first.next;
            return removeItem;
        }

        // Traverse the list to find the node to be removed
        while (current != null && !current.data.equals(fObj)) {
            previous = current;
            current = current.next;
        }

        // If the node is not found
        if (current == null) {
            return null;
        }

        // Remove the node
        removeItem = (Object)current.data;
        previous.next = current.next;
        current.next = null;

        return removeItem;
    }
    
    
}
