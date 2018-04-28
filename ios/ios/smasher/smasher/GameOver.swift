//
//  GameOver.swift
//  smasher
//
//  Created by admin on 25/04/2018.
//  Copyright © 2018 etudiant. All rights reserved.
//

import SpriteKit
import GameplayKit

class GameOver: GKState {
    unowned let scene: GameScene
    
    init(scene: SKScene) {
        self.scene = scene as! GameScene
        super.init()
    }
    
    override func didEnter(from previousState: GKState?) {
        if previousState is Play{
            let ball = scene.childNode(withName: "ball") as! SKSpriteNode
            ball.physicsBody!.linearDamping = 1.0
            scene.physicsWorld.gravity = CGVector(dx: 0, dy: -8)
        }
    }
    
    override func isValidNextState(_ stateClass: AnyClass) -> Bool {
        return stateClass is Tap.Type
    }
    
    
}
