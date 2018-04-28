//
//  Tap.swift
//  smasher
//
//  Created by admin on 23/04/2018.
//  Copyright © 2018 etudiant. All rights reserved.
//

import SpriteKit
import GameplayKit

class Tap: GKState {
    unowned let scene: GameScene
    
    init(scene: SKScene) {
        self.scene = scene as! GameScene
        super.init()
    }
    
    override func didEnter(from previousState: GKState?) {
        let scale = SKAction.scale(to: 1.0, duration: 0.25)
        scene.childNode(withName: GameMessageName)!.run(scale)
    }
    
    override func willExit(to nextState: GKState) {
        if nextState is Play {
            let scale = SKAction.scale(to: 0, duration: 0.4)
            scene.childNode(withName: GameMessageName)!.run(scale)
        }
    }

    override func isValidNextState(_ stateClass: AnyClass) -> Bool {
        return stateClass is Play.Type
    }
    
}
