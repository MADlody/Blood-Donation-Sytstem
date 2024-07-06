
public class ListNode
{
   Object data;
   ListNode next;
   ListNode(Object o)
   {
       this(o,null);
   }
   ListNode(Object o, ListNode nextNode)
   {
       data=o;
       next=nextNode;
   }
   Object getData()
   {
       return data;
   }
   Object getNext()
   {
       return next;
   }
}
