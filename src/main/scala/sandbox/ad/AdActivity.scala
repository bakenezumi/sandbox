package sandbox.ad

import sandbox.fsm.Event

sealed trait AdActivity extends Event


class Won extends AdActivity {

}

class ViewProgress extends AdActivity {

}

class ViewComplete extends AdActivity {

}

class Click extends AdActivity {

}
