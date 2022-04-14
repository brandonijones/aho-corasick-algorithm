# Aho-Corasick Algorithm
This is a string-searching algorithm that looks through a given piece of text to locate elements from a specific set of strings (often called a "dictionary").
With the use of a _trie data structure_, _failure/suffix links_, and _dictionary/output links_ we can find all occurances of any word in our specified dictionary at once instead
of looking for each word one at a time.

## Trie Visualization
This data structure is used to store the strings in your dictionary. A simple dictionary with the words { A, ATC, C, CAT, GCG, GC, GCA } is represented in the trie below.
The root node represents an empty string and each yellow node represents a word node.

![trie](https://user-images.githubusercontent.com/65355965/155619398-582c77f5-a0e5-44d0-8319-ae084fada509.png)

## Failure Links Visualization
Failure links, or suffix links, are node pointers that represent the longest suffix that exists elsewhere on the tree starting from the root node. For example, if we look at the
node "A" on the path G --> C --> A, the string _CA_ is the longest suffix for the string _GCA_, so the failure link will point to the node _A_ on the path C --> A. The root node
represents an empty string, so if there no common suffix that can be found, the failure link should point to the root. **Every node needs to have a failure link.**

![failure_links_tree](https://user-images.githubusercontent.com/65355965/155627092-47abf3d2-45d9-44c7-8848-9df12f9de672.png)

These are important for making the string searching linear. When searching for strings that match the ones from your dictionary, the tree pointer starts at the root node and
looks any children matching the current letter from the given text. If a matching child is found, then the pointer will point to that child. If there is no matching child, then
the pointer needs to follow a failure link. The video below shows how this process works.

https://user-images.githubusercontent.com/65355965/155628080-df8c2032-a7ba-4e42-b7de-9ce2065c0161.mp4

## Dictionary Links Visualization
As seen in the video above, failure links alone are not always enough to find all of the words or an accurate count of the words that were found. In this case, it missed that
_A_ and two more _C_ s can be found in the given text. This is where dictionary links, or output links, are necessary. Unlike failure links, not every node needs a dictionary
link. Dictionary links are made if by following the path of failure links from one node leads to a word node. For example, for the _A_ node on the path G --> C --> A, following
the failure links leads to the _A_ word node that is a child of the root node. When searching, the tree pointer will still only follows the failure links, but when it reaches a
node, the dictionary link will help to signal that another word was also found on this path.

![dictionary_links_tree](https://user-images.githubusercontent.com/65355965/155630581-e229d0aa-1594-4aed-8db4-146579e89d1a.png)

## Full Demonstration (animation)
Now that the failure links and dictionary links have been added to the tree, we have a fully working Aho-Corasick automaton. Below is the animation demonstration a simple string
search. Notice how the dictionary links find the strings that the failure links would have missed.

https://user-images.githubusercontent.com/65355965/155631601-5a811278-6be1-4b87-9ad0-526faddbfd4e.mp4

## Building the Trie
In my implementation, I used an array of nodes to represent each letter in the English alphabet which is represented in `private TrieNode[] children = new TrieNode[26]`.
The children represent the next possible letter to follow the current node. In the above illustration, the root node would have the children `children[0]`, `children[2]`, and
`children[6]` to represent the letters 'A', 'C', and 'G' respectively. Each node has a boolean value to determin whether the path (or prefix) is a word, a pattern index to
determine the specific word found from our dictionary, and pointers representing the failure links and dictionary links.

![trie_node_class](https://user-images.githubusercontent.com/65355965/155618530-9e9f47f7-7e69-477e-a86c-3379b891415c.png)

When building the trie, one letter of each word is added at a time. Simply follow the pattern of starting from the root node and checking to see if the letter that needs to be
added is a child of the current node. If the corresponding index in the array is null, then add the node. If it is not null, then point to that node instead.

![build_trie](https://user-images.githubusercontent.com/65355965/155633143-1f890e63-0e62-4768-91bd-9e197d33a3b4.png)

## Building Failure and Dictionary Links
To build the failure links and dictionary links, you can use a Breadth-First-Search approach. Start at the root node and add all of its children to a queue. At each node you visit, add it's children to the queue. All children of the root will have their failure links pointing to the root. After visiting the root's children, we continue visiting the nodes that are in the queue. If the current node in the queue has children, we keep track of the failure link of the current node in the queue (called `temp`) and each child (`currentChild`). If `temp` does not have a child that matches the index (letter) of the `currentChild`, then there is no suffix that matches the current path. Therefore you give the `currentChild` a failure link of `root`, which is an empty string. If a suffix does exist, then give the `currentChild` a failure link to the corresponding index of `temp`. Then add the `currentChild` to the queue and continue the process for the rest of the children.
Then for dictionary links, check if the current node's failure link is a word. If it is a word, point the dictionary link to that failure link. If not, the dictionary link remains null.
![build_failure_and_dictionary_links](https://user-images.githubusercontent.com/65355965/155633175-5a151e83-1994-44e1-bafa-0f6cf2d931d1.png)

## Search Method
Searching is simple. Start at the root of the tree and compare the children to the current letter in the string passed in the method. If the current letter in the string is a child of the current node in the tree, then traverse to that corresponding node. If the current node is a word node, notify to the console that a word was found. If the current letter is not a child of the current node in the tree, follow the failure link and check again. if there are no children that matches the current letter of the string at the root, then go to the next letter in the string. Whenever you pass through a node with a dictionary link that is null, mark that another word has been found too.
![search](https://user-images.githubusercontent.com/65355965/155633203-caa86685-04a2-4463-b531-08f543d354b5.png)

